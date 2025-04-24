package com.module.notelycompose.audio.ui.expect

import kotlinx.cinterop.*
import kotlinx.cinterop.alloc
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


    /**
     * Call when entering recording screen
     */
    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun setup() {

        try {
            recordingSession.setCategory(
                AVAudioSessionCategoryPlayAndRecord,
                withOptions = AVAudioSessionCategoryOptions.MAX_VALUE,
                null
            )
            recordingSession.setActive(true, null)
        } catch (e: Exception) {
            println("Audio session setup failed: ${e.message}")
        }


    }

    /**
     * Call when leaving recording screen
     */
    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun teardown() {
        // 1. Stop any active recording
        if (isRecording()) {
            stopRecording()
        }

        // 2. Deactivate audio session
        try {
            recordingSession.setActive(false, null)
        } catch (e: Exception) {
            println("Audio session teardown failed: ${e.message}")
        }

    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual fun startRecording() {
        // 1. Request permissions early
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

        val settings = mapOf<Any?, Any?>(
            AVFormatIDKey to kAudioFormatMPEG4AAC,
            AVSampleRateKey to 44100.0,
            AVNumberOfChannelsKey to 1,
            AVEncoderAudioQualityKey to AVAudioQualityHigh,
            AVEncoderBitRateKey to 32000
        )


        audioRecorder = AVAudioRecorder(recordingURL, settings, null)

        if (audioRecorder?.prepareToRecord() == true) {
            audioRecorder?.record()
            println("Recording started successfully")
        } else {
            println("Failed to prepare recording")
            audioRecorder = null
        }
    }


    @OptIn(ExperimentalForeignApi::class)
    actual fun stopRecording() {
        println("Stop Recording")
        audioRecorder?.let { recorder ->
            if (recorder.isRecording()) {
                recorder.stop()
            }
        }

        audioRecorder = null
    }

    actual fun isRecording(): Boolean {
        return audioRecorder?.isRecording() ?: false
    }

    actual fun hasRecordingPermission(): Boolean {
        return recordingSession.recordPermission() == AVAudioSessionRecordPermissionGranted
    }

    actual suspend fun requestRecordingPermission() : Boolean {
        if (hasRecordingPermission()) return true

        return suspendCancellableCoroutine { continuation ->
            recordingSession.requestRecordPermission { granted ->
                continuation.resume(granted)
            }
        }
    }

    actual fun getRecordingFilePath(): String {
        return recordingURL.path.orEmpty()
    }
}