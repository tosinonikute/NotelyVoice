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

private const val RECORDING_PREFIX = "recording_"
private const val RECORDING_EXTENSION = ".m4a"

actual class AudioRecorder {

    private var audioRecorder: AVAudioRecorder? = null
    private var recordingSession: AVAudioSession = AVAudioSession.sharedInstance()
    private lateinit var recordingURL: NSURL

    @OptIn(ExperimentalForeignApi::class)
    actual fun startRecording() {
        if (!hasRecordingPermission()) {
            println("Recording permission not granted")
            return
        }

        val randomNumber = Random.nextInt(100000, 999999)
        val fileName = "$RECORDING_PREFIX$randomNumber$RECORDING_EXTENSION"

        val documentsDirectory = NSFileManager.defaultManager.URLsForDirectory(
            NSDocumentDirectory,
            NSUserDomainMask
        ).first() as NSURL

        this.recordingURL = documentsDirectory.URLByAppendingPathComponent(fileName)!!

        recordingSession.requestRecordPermission { granted: Boolean ->
            if (granted) {
                val settings = mapOf<Any?, Any?>(
                    AVFormatIDKey to kAudioFormatMPEG4AAC,
                    AVSampleRateKey to 44100.0,
                    AVNumberOfChannelsKey to 1,
                    AVEncoderBitRateKey to 32000
                )
                val url = NSURL.fileURLWithPath(this.recordingURL.path.orEmpty())
                memScoped {
                    try {
                        val error: ObjCObjectVar<NSError?> = alloc()
                        audioRecorder = AVAudioRecorder(url, settings, error.ptr)
                        if(audioRecorder?.prepareToRecord() == true) {
                            audioRecorder?.record()
                        }
                    } catch (e: Exception) {
                        println(e.toString())
                        e.printStackTrace()
                    }
                }
            } else {
                println("Recording permission not granted")
            }
        }
        recordingSession.setCategory(AVAudioSessionCategoryRecord, null )
        recordingSession.setActive(true, null)
    }

    actual fun stopRecording() {
        if (audioRecorder?.isRecording() == true) {
            audioRecorder?.stop()
            audioRecorder = null
        }
    }

    actual fun isRecording(): Boolean {
        return audioRecorder?.isRecording() ?: false
    }

    actual fun hasRecordingPermission(): Boolean {
        val status = recordingSession.recordPermission()
        return status == AVAudioSessionRecordPermissionGranted
    }

    actual suspend fun requestRecordingPermission() {
        if (hasRecordingPermission()) {
            return
        }

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