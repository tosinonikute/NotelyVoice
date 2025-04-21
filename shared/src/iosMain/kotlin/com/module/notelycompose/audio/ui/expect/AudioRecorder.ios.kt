package com.module.notelycompose.audio.ui.expect

import kotlinx.cinterop.*
import platform.Foundation.*
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AVFAudio.AVAudioQualityHigh
import platform.AVFAudio.AVAudioRecorder
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayAndRecord
import platform.AVFAudio.AVAudioSessionCategoryRecord
import platform.AVFAudio.AVAudioSessionRecordPermissionGranted
import platform.AVFAudio.AVEncoderAudioQualityKey
import platform.AVFAudio.AVEncoderBitRateKey
import platform.AVFAudio.AVFormatIDKey
import platform.AVFAudio.AVNumberOfChannelsKey
import platform.AVFAudio.AVSampleRateKey
import platform.AVFAudio.setActive
import platform.CoreAudioTypes.kAudioFormatMPEG4AAC
import kotlin.coroutines.resume
import kotlin.random.Random
import platform.AVFoundation.*
import platform.darwin.*
import platform.AVFAudio.*


private const val RECORDING_PREFIX = "recording_"
private const val RECORDING_EXTENSION = ".m4a"

actual class AudioRecorder {

    private var audioRecorder: AVAudioRecorder? = null
    private var recordingSession: AVAudioSession = AVAudioSession.sharedInstance()
    private lateinit var recordingURL: NSURL

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual fun startRecording() {
        if (!hasRecordingPermission()) {
            println("Recording permission not granted")
            return
        }

        val randomNumber = Random.nextInt(100000, 999999)
        val fileName = "$RECORDING_PREFIX$randomNumber$RECORDING_EXTENSION"

        println("Start Recording: $fileName")
        val documentsDirectory = NSFileManager.defaultManager.URLsForDirectory(
            NSDocumentDirectory,
            NSUserDomainMask
        ).first() as NSURL

        this.recordingURL = documentsDirectory.URLByAppendingPathComponent(fileName) ?: run {
            println("Failed to create recording URL")
            return
        }

        try {
            recordingSession.setCategory(AVAudioSessionCategoryPlayAndRecord, null)
            recordingSession.setActive(true, null)
        } catch (e: Exception) {
            println("Failed to configure audio session: ${e.message}")
            return
        }

        val settings = mapOf<Any?, Any?>(
            AVFormatIDKey to kAudioFormatMPEG4AAC,
            AVSampleRateKey to 44100.0,
            AVNumberOfChannelsKey to 1,
            AVEncoderAudioQualityKey to AVAudioQualityHigh,
            AVEncoderBitRateKey to 32000
        )

        memScoped {
            val error: ObjCObjectVar<NSError?> = alloc()
            val path = recordingURL.path ?: run {
                println("Invalid recording path")
                return@memScoped
            }
            val url = NSURL.fileURLWithPath(path)

            audioRecorder = AVAudioRecorder(url, settings, error.ptr)
            if (error.value != null) {
                println("Error creating audio recorder: ${error.value?.localizedDescription}")
                return@memScoped
            }

            if (audioRecorder?.prepareToRecord() == true) {
                audioRecorder?.record()
                println("Recording started successfully")
            } else {
                println("Failed to prepare recording")
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun stopRecording() {
        println("Stop Recording")
        if (audioRecorder?.isRecording() == true) {
            audioRecorder?.stop()
            audioRecorder = null
            try {
                recordingSession.setActive(false, null)
            } catch (e: Exception) {
                println("Failed to deactivate audio session: ${e.message}")
            }
        }
    }

    actual fun isRecording(): Boolean {
        return audioRecorder?.isRecording() ?: false
    }

    actual fun hasRecordingPermission(): Boolean {
        return recordingSession.recordPermission() == AVAudioSessionRecordPermissionGranted
    }

    actual suspend fun requestRecordingPermission() {
        if (hasRecordingPermission()) return

        return suspendCancellableCoroutine { continuation ->
            recordingSession.requestRecordPermission { granted ->
                continuation.resume(Unit)
            }
        }
    }

    actual fun getRecordingFilePath(): String {
        return recordingURL.path.orEmpty()
    }
}