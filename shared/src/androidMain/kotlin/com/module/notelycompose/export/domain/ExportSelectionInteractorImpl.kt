package com.module.notelycompose.export.domain

import android.content.Context
import com.module.notelycompose.FolderPickerHandler
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.json.JSONObject

private const val TEXT_BLANK_DEFAULT = "no-title"
private const val PATTERN_DATE_FORMAT = "yyyy-MM-ddHH-mm-ss-SSS"

class ExportSelectionInteractorImpl(
    private val context: Context,
    private val folderPickerHandler: FolderPickerHandler
): ExportSelectionInteractor {

    @OptIn(DelicateCoroutinesApi::class)
    override fun exportAllSelection(
        texts: List<String>,
        titles: List<String>,
        audioPath: List<String>,
        shouldExportAudio: Boolean,
        shouldExportTxt: Boolean,
        onProgress: (Float) -> Unit,
        onResult: (Result<String>) -> Unit
    ) {
        folderPickerHandler.pickFolder { folderUri ->

            if (folderUri == null) {
                onResult(Result.failure(NoFolderSelectedException("Folder selection cancelled")))
                return@pickFolder
            }

            kotlinx.coroutines.GlobalScope.launch(Dispatchers.IO) {
                val result = performTextExport(
                    folderUri,
                    texts,
                    titles,
                    audioPath,
                    shouldExportAudio,
                    shouldExportTxt,
                    onProgress
                )
                withContext(Dispatchers.Main) {
                    onResult(result)
                }
            }
        }
    }

    private suspend fun performTextExport(
        folderUri: Uri,
        texts: List<String>,
        titles: List<String>,
        audioPath: List<String>,
        shouldExportAudio: Boolean,
        shouldExportTxt: Boolean,
        onProgress: (Float) -> Unit,
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val folder = DocumentFile.fromTreeUri(context, folderUri)
                ?: return@withContext Result.failure(Exception("Invalid folder URI"))

            val timestamp = SimpleDateFormat(PATTERN_DATE_FORMAT, Locale.getDefault()).format(Date())
            val exportFolder = folder.createDirectory("Notes_export_$timestamp")
                ?: return@withContext Result.failure(Exception("Failed to create export folder"))

            val textFileNames = mutableListOf<String>()
            val audioFileNames = mutableListOf<String>()

            // Calculate total items to export
            val totalItems = (if (shouldExportTxt) texts.size else 0) +
                    (if (shouldExportAudio) audioPath.size else 0)
            var completedItems = 0

            if(shouldExportTxt) {
                texts.forEachIndexed { index, text ->
                    val timestampText = SimpleDateFormat(PATTERN_DATE_FORMAT, Locale.getDefault()).format(Date())
                    val textTitle = (titles[index].takeIf { it.isNotBlank() } ?: TEXT_BLANK_DEFAULT).take(20)
                    val fileName = "${textTitle}-${timestampText}.txt"
                    val textFile = exportFolder.createFile("text/plain", fileName)
                        ?: return@withContext Result.failure(Exception("Failed to create text file $index"))

                    context.contentResolver.openOutputStream(textFile.uri)?.use { outputStream ->
                        outputStream.write(text.toByteArray())
                    }
                    textFileNames.add(fileName)

                    // Update progress
                    completedItems++
                    val progress = completedItems.toFloat() / totalItems.toFloat()
                    kotlinx.coroutines.MainScope().launch {
                        onProgress(progress)
                    }
                }
            }

            if(shouldExportAudio) {
                audioPath.forEachIndexed { index, path ->
                    if(path.isNotEmpty()) {
                        val timestampAudio = SimpleDateFormat(PATTERN_DATE_FORMAT, Locale.getDefault()).format(Date())
                        val sourceFile = File(path)
                        if (!sourceFile.exists()) {
                            return@withContext Result.failure(Exception("Audio file not found: $path"))
                        }

                        val textTitle = (titles[index].takeIf { it.isNotBlank() } ?: TEXT_BLANK_DEFAULT).take(20)
                        val audioFileName = "${textTitle}-audio-${timestampAudio}.wav"
                        val mimeType = getMimeType(audioFileName)
                        val audioFile = exportFolder.createFile(mimeType, audioFileName)
                            ?: return@withContext Result.failure(Exception("Failed to create audio file $index"))

                        context.contentResolver.openOutputStream(audioFile.uri)?.use { outputStream ->
                            FileInputStream(sourceFile).use { inputStream ->
                                inputStream.copyTo(outputStream)
                            }
                        }
                        audioFileNames.add(audioFileName)

                        // Update progress
                        completedItems++
                        val progress = completedItems.toFloat() / totalItems.toFloat()
                        kotlinx.coroutines.MainScope().launch {
                            onProgress(progress)
                        }
                    }
                }
            }

            // Create JSON file with all filenames
            createMetadataJson(exportFolder, textFileNames, audioFileNames)

            Result.success("Successfully exported ${texts.size} text files")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getMimeType(fileName: String): String {
        return when (fileName.substringAfterLast('.', "").lowercase()) {
            "wav" -> "audio/wav"
            "mp3" -> "audio/mpeg"
            "m4a" -> "audio/mp4"
            "ogg" -> "audio/ogg"
            else -> "audio/*"
        }
    }

    private fun createMetadataJson(
        exportFolder: DocumentFile,
        textFileNames: List<String>,
        audioFileNames: List<String>
    ) {
        try {
            val jsonArray = JSONArray()

            val maxSize = maxOf(textFileNames.size, audioFileNames.size)

            for (i in 0 until maxSize) {
                val jsonObject = JSONObject().apply {
                    put("textFile", textFileNames.getOrNull(i) ?: "")
                    put("audioFile", audioFileNames.getOrNull(i) ?: "")
                    put("timestamp", SimpleDateFormat(PATTERN_DATE_FORMAT, Locale.getDefault()).format(Date()))
                }
                jsonArray.put(jsonObject)
            }

            val timestampJson = SimpleDateFormat(PATTERN_DATE_FORMAT, Locale.getDefault()).format(Date())
            val jsonFile = exportFolder.createFile("application/json", "metadata-${timestampJson}.json")
                ?: throw Exception("Failed to create JSON metadata file")

            context.contentResolver.openOutputStream(jsonFile.uri)?.use { outputStream ->
                outputStream.write(jsonArray.toString(2).toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
