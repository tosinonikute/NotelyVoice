package com.module.notelycompose.audio.ui.expect

import kotlinx.cinterop.*
import platform.Foundation.*
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AVFAudio.AVAudioQualityHigh
import platform.AVFAudio.AVAudioRecorder
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayAndRecord
import platform.AVFAudio.AVAudioSessionRecordPermissionGranted
import platform.AVFAudio.AVEncoderAudioQualityKey
import platform.AVFAudio.AVFormatIDKey
import platform.AVFAudio.AVNumberOfChannelsKey
import platform.AVFAudio.AVSampleRateKey
import platform.AVFAudio.setActive
import platform.CoreAudioTypes.kAudioFormatMPEG4AAC
import kotlin.coroutines.resume
import kotlin.random.Random

private const val DEFAULT = "recording.mp3"
private const val RECORDING_PREFIX = "recording_"
private const val RECORDING_EXTENSION = ".mp3"

actual class AudioRecorder {
    private var audioRecorder: AVAudioRecorder? = null
    private var isCurrentlyRecording = false
    private var currentRecordingPath: String? = null
    private val audioSession = AVAudioSession.sharedInstance()

    @OptIn(ExperimentalForeignApi::class)
    actual fun startRecording() {
        val randomNumber = Random.nextInt(100000, 999999)
        val fileName = "$RECORDING_PREFIX$randomNumber$RECORDING_EXTENSION"

        // Get cache directory path in iOS
        val fileManager = NSFileManager.defaultManager
        val cachesDirectory = fileManager.URLsForDirectory(
            NSCachesDirectory,
            NSUserDomainMask
        ).firstOrNull() as NSURL?

        val fileURL = cachesDirectory?.URLByAppendingPathComponent(fileName)
        currentRecordingPath = fileURL?.path

        // Configure audio session for recording
        try {
            audioSession.setCategory(
                AVAudioSessionCategoryPlayAndRecord,
                null
            )
            audioSession.setActive(true, null)

            // Create settings dictionary for AAC format
            val settings = mutableMapOf<Any?, Any>()
            settings[AVFormatIDKey] = kAudioFormatMPEG4AAC
            settings[AVSampleRateKey] = 44100.0
            settings[AVNumberOfChannelsKey] = 1
            settings[AVEncoderAudioQualityKey] = AVAudioQualityHigh

            // Initialize and start recorder
            fileURL?.let {
                audioRecorder = AVAudioRecorder(
                    uRL = it,
                    settings = settings,
                    error = null
                )
                audioRecorder?.prepareToRecord()
                audioRecorder?.record()
                isCurrentlyRecording = true
            }
        } catch (e: Throwable) {
            println("Recording error: ${e.message}")
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun stopRecording() {
        audioRecorder?.stop()
        audioRecorder = null
        isCurrentlyRecording = false

        try {
            audioSession.setActive(false, null)
        } catch (e: Throwable) {
            println("Could not deactivate audio session: ${e.message}")
        }
    }

    actual fun isRecording(): Boolean {
        return isCurrentlyRecording
    }

    actual fun hasRecordingPermission(): Boolean {
        return when (audioSession.recordPermission) {
            AVAudioSessionRecordPermissionGranted -> true
            else -> false
        }
    }

    actual suspend fun requestRecordingPermission() {
        if (hasRecordingPermission()) {
            return
        }

        return suspendCancellableCoroutine { continuation ->
            audioSession.requestRecordPermission { granted ->
                continuation.resume(Unit)
            }
        }
    }

    actual fun getRecordingFilePath(): String {
        if (currentRecordingPath != null) {
            return currentRecordingPath!!
        }

        // Return default path if no recording has been made
        val fileManager = NSFileManager.defaultManager
        val cachesDirectory = fileManager.URLsForDirectory(
            NSCachesDirectory,
            NSUserDomainMask
        ).firstOrNull() as NSURL?

        return cachesDirectory?.URLByAppendingPathComponent(DEFAULT)?.path ?: DEFAULT
    }
}
