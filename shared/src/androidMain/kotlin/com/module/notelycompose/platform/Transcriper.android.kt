package com.module.notelycompose.platform

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import androidx.core.content.ContextCompat
import audio.utils.LauncherHolder
import com.module.notelycompose.core.debugPrintln
import com.module.notelycompose.utils.StreamingAudioChunker
import com.module.notelycompose.utils.StreamingAudioChunk
import com.module.notelycompose.utils.ChunkTranscriptionResult
import com.whispercpp.whisper.WhisperCallback
import com.whispercpp.whisper.WhisperContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume

actual class Transcriber(
    private val context: Context,
    private val launcherHolder: LauncherHolder
) {
    private var canTranscribe: Boolean = false
    private var isTranscribing = false
    private val modelsPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
    private var whisperContext: WhisperContext? = null
    private var permissionContinuation: ((Boolean) -> Unit)? = null
    private val streamingChunker = StreamingAudioChunker()


    actual fun hasRecordingPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }


    actual suspend fun requestRecordingPermission(): Boolean {
        if (hasRecordingPermission()) {
            return true
        }

        return suspendCancellableCoroutine { continuation ->
            permissionContinuation = { isGranted ->
                continuation.resume(isGranted)
            }

            if (launcherHolder.permissionLauncher != null) {
                launcherHolder.permissionLauncher?.launch(arrayOf(Manifest.permission.RECORD_AUDIO))
            } else {
                continuation.resume(false)
            }

            continuation.invokeOnCancellation {
                permissionContinuation = null
            }
        }
    }


    actual suspend fun initialize(modelFileName: String) {
        debugPrintln{"speech: initialize model"}
        loadBaseModel(modelFileName)
    }

    private fun loadBaseModel(modelFileName: String) {
        try {
            debugPrintln{"Loading model: $modelFileName\n"}
            val modelFile = File(modelsPath, modelFileName)
            whisperContext = WhisperContext.createContextFromFile(modelFile.absolutePath)
            canTranscribe = true
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    actual fun doesModelExists(modelFileName: String) : Boolean{
        val modelFile = File(modelsPath, modelFileName)
        return modelFile.exists()
    }

    actual fun isValidModel(modelFileName: String) : Boolean{
        try {
             loadBaseModel(modelFileName)
        } catch (e:Exception){
          return false
        }
        return true
    }

    actual suspend fun stop() {
        isTranscribing = false
        whisperContext?.stopTranscription()
    }

    actual suspend fun finish() {
        whisperContext?.release()
    }

    actual suspend fun start(
        filePath: String, language: String,
        onProgress : (Int) -> Unit,
        onNewSegment : (Long, Long,String) -> Unit,
        onComplete : () -> Unit,
        onError : () -> Unit
    ) {
        if (!canTranscribe) {
            return
        }

        canTranscribe = false
        isTranscribing = true

        try {
            debugPrintln{"Reading WAV file chunks directly from disk...\n"}
            
            // Split WAV file into streaming chunks without loading entire file into memory
            val streamingChunks = streamingChunker.splitWavFileIntoChunks(filePath)
            debugPrintln{"Processing ${streamingChunks.size} streaming chunks...\n"}
            
            val start = System.currentTimeMillis()
            val chunkResults = mutableListOf<ChunkTranscriptionResult>()
            var completedChunks = 0
            
            streamingChunks.forEachIndexed { chunkIndex, streamingChunk ->
                if (!isTranscribing) {
                    debugPrintln{"Transcription stopped by user"}
                    return@forEachIndexed
                }
                
                debugPrintln{"Processing streaming chunk ${chunkIndex + 1}/${streamingChunks.size} (${streamingChunk.durationSeconds}s)"}
                
                val chunkSegments = mutableListOf<com.module.notelycompose.utils.TranscriptionSegment>()
                var chunkText = ""
                
                try {
                    // Read chunk data directly from file (using reusable arrays)
                    val chunkData = streamingChunker.readChunkData(streamingChunk)
                    debugPrintln{"Transcription: Read ${chunkData.size} samples from chunk $chunkIndex (reusable array)"}
                    
                    // Update progress to show chunk is starting
                    val chunkProgress = 100.0 / streamingChunks.size
                    val startProgress = (completedChunks * chunkProgress).toInt().coerceIn(0, 100)
                    onProgress(startProgress)
                    
                    val result = whisperContext?.transcribeData(chunkData, language, callback = object : WhisperCallback {
                        override fun onNewSegment(startMs: Long, endMs: Long, text: String) {
                            // Adjust timing to account for chunk position in original audio
                            val chunkStartTimeMs = (streamingChunk.startOffset - 44) / (streamingChunk.header.sampleRate * streamingChunk.header.channels * (streamingChunk.header.bitsPerSample / 8.0) / 1000.0)
                            val adjustedStartMs = startMs + chunkStartTimeMs.toLong()
                            val adjustedEndMs = endMs + chunkStartTimeMs.toLong()
                            
                            chunkSegments.add(com.module.notelycompose.utils.TranscriptionSegment(
                                adjustedStartMs, adjustedEndMs, text
                            ))
                            
                            // Call the original callback with adjusted timing
                            onNewSegment(adjustedStartMs, adjustedEndMs, text)
                        }

                        override fun onProgress(progress: Int) {
                            // Simple chunk-based progress: each chunk represents equal progress
                            val totalChunks = streamingChunks.size
                            val chunkProgress = 100.0 / totalChunks
                            
                            // Progress = completed chunks + current chunk progress
                            val overallProgress = ((completedChunks * chunkProgress) + (progress * chunkProgress / 100.0)).toInt().coerceIn(0, 100)
                            
                            debugPrintln{"Transcription: Chunk $chunkIndex progress: $progress%, Overall: $overallProgress%"}
                            onProgress(overallProgress)
                        }

                        override fun onComplete() {
                            // This will be called for each chunk
                            completedChunks++
                            debugPrintln{"Transcription: Transcription completed for chunk $chunkIndex (${completedChunks}/${streamingChunks.size} completed)"}
                        }
                    })
                    
                    chunkText = result ?: ""
                    
                    // Create a temporary AudioChunk for compatibility with existing merge logic
                    val tempAudioChunk = com.module.notelycompose.utils.AudioChunk(
                        startSample = ((streamingChunk.startOffset - 44) / (streamingChunk.header.channels * (streamingChunk.header.bitsPerSample / 8))).toInt(),
                        endSample = ((streamingChunk.endOffset - 44) / (streamingChunk.header.channels * (streamingChunk.header.bitsPerSample / 8))).toInt(),
                        data = chunkData
                    )
                    
                    chunkResults.add(ChunkTranscriptionResult(tempAudioChunk, chunkText, chunkSegments))
                    
                    // Clear chunk data from memory after processing (reusable array)
                    chunkData.fill(0.0f)
                    debugPrintln{"Transcription: Cleared chunk $chunkIndex data from memory (${chunkData.size} samples, reusable array)"}
                    
                } catch (e: Exception) {
                    debugPrintln{"Error processing streaming chunk $chunkIndex: ${e.localizedMessage}"}
                    e.printStackTrace()
                }
            }
            
            // Merge results from all chunks
            if (isTranscribing && chunkResults.isNotEmpty()) {
                
                // Clear chunk results from memory after merging
                chunkResults.clear()
                debugPrintln{"Transcription: Cleared all chunk results from memory"}
            }
            
            val elapsed = System.currentTimeMillis() - start
            debugPrintln{"Done ($elapsed ms)\n"}
            
            // Clear streaming chunks from memory
            streamingChunks.clear()
            debugPrintln{"Transcription: Cleared streaming chunks list from memory"}
            
            // Clear reusable arrays from memory
            streamingChunker.clearReusableArrays()
            val arraySizes = streamingChunker.getReusableArraySizes()
            debugPrintln{"Transcription: Cleared reusable arrays from memory (FloatArray: ${arraySizes.first}, ByteArray: ${arraySizes.second})"}
            
            if (isTranscribing) {
                onComplete()
            }

        } catch (e: OutOfMemoryError) {
            onError()
            e.printStackTrace()
            debugPrintln{"OutOfMemoryError: File too large to process - ${e.message}\n"}
        } catch (e: Exception) {
            onError()
            e.printStackTrace()
            debugPrintln{"${e.localizedMessage}\n"}
        }

        canTranscribe = true
        isTranscribing = false
    }
}