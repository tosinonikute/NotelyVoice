package com.module.notelycompose.audio.ui.expect

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AVFAudio.AVAudioEngine
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayAndRecord
import platform.AVFAudio.AVAudioSessionModeMeasurement
import platform.AVFAudio.AVAudioSessionRecordPermissionGranted
import platform.AVFAudio.setActive
import platform.Foundation.NSError
import platform.Foundation.NSLocale
import platform.Foundation.NSURL
import platform.Foundation.localeIdentifier
import platform.Speech.SFSpeechAudioBufferRecognitionRequest
import platform.Speech.SFSpeechRecognitionTask
import platform.Speech.SFSpeechRecognitionTaskHintDictation
import platform.Speech.SFSpeechRecognizer
import platform.Speech.SFSpeechRecognizerAuthorizationStatus
import platform.Speech.SFSpeechURLRecognitionRequest
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.posix.close
import platform.posix.exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class SpeechRecognizer {
    private var audioEngine: AVAudioEngine? = null
    private var request: SFSpeechAudioBufferRecognitionRequest? = null
    private var task: SFSpeechRecognitionTask? = null
    private var recognizer: SFSpeechRecognizer? = null
    private var isInitialized = false
    private var isListening = false
    val audioSession = AVAudioSession.sharedInstance()

    actual fun initialize() {
        recognizer = SFSpeechRecognizer()

        isInitialized = true
    }
    actual fun finishRecognizer() {
        isInitialized = false
        reset()
    }

    actual fun stopRecognizer() {
        if(isListening) {
            println("speech:stopRecognizer")
            isListening = false
            task?.cancel()
            task = null
            audioEngine?.stop()
            reset()
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun startRecognizing(onComplete: (Boolean, String?) -> Unit) {
        if(!isListening) {
            isListening = true
            try {
                prepareEngine()

                println("speech:startRecognizer ${recognizer}")
                if (request != null) {
                    task = recognizer?.recognitionTaskWithRequest(request!!) { result, error ->
                       // println("speech:task")
                        dispatch_async(dispatch_get_main_queue()) {
                            when {
                                error != null -> {
                                    println(error.localizedDescription)
                                }

                                result == null -> {
                                    println("No recognition result")
                                }

                                result.isFinal() -> {
                                    val text = result.bestTranscription.formattedString
                                   // println("Final Result $text")
                                    onComplete(result.final, text)
                                }

                                else -> {
                                    val text = result.bestTranscription.formattedString
                                 //   println("Result $text")
                                    onComplete(result.final, text)
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                println("speech: $e")
                e.printStackTrace()
            }
        }
    }

    actual  fun hasRecordingPermission(): Boolean {
        return SFSpeechRecognizer.authorizationStatus() == SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusAuthorized &&
                audioSession.recordPermission() == AVAudioSessionRecordPermissionGranted
    }

    actual suspend fun requestRecordingPermission(): Boolean = suspendCancellableCoroutine { continuation ->
        var speechAuthCompleted = false
        var recordAuthCompleted = false
        var speechAuthGranted = false
        var recordAuthGranted = false

        SFSpeechRecognizer.requestAuthorization { status ->
            speechAuthGranted = status == SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusAuthorized
            speechAuthCompleted = true

            if (recordAuthCompleted) {
                continuation.resume(speechAuthGranted && recordAuthGranted)
            }
        }

        AVAudioSession.sharedInstance().requestRecordPermission { granted ->
            recordAuthGranted = granted
            recordAuthCompleted = true

            if (speechAuthCompleted) {
                continuation.resume(speechAuthGranted && recordAuthGranted)
            }
        }

        continuation.invokeOnCancellation {
            // Clean up if the coroutine is cancelled
        }
    }
    private fun reset() {
        audioEngine = null
        request = null
    }
    @OptIn(ExperimentalForeignApi::class)
    private fun prepareEngine(){
        val audioEngine = AVAudioEngine()
        val request = SFSpeechAudioBufferRecognitionRequest()
        request.addsPunctuation = true
        request.taskHint = SFSpeechRecognitionTaskHintDictation
        request.shouldReportPartialResults = true

        val audioSession = AVAudioSession.sharedInstance()
        audioSession.setCategory(AVAudioSessionCategoryPlayAndRecord, null)
        audioSession.setMode(AVAudioSessionModeMeasurement, null)
        audioSession.setActive(true, null)
        val inputNode = audioEngine.inputNode
        val recordingFormat = inputNode.outputFormatForBus(0u)
        inputNode.installTapOnBus(0u, bufferSize = 1024u, format = recordingFormat) { buffer, _ ->
            if (buffer != null) {
                request.appendAudioPCMBuffer(buffer)
            }
        }
        audioEngine.prepare()
        this.audioEngine = audioEngine
        this.request = request
        audioEngine.startAndReturnError(null)
    }
    
    private fun supportedLocales() {
        // Get the supported locales and extract their identifiers
        val supportedLocales = SFSpeechRecognizer.supportedLocales()
        val localeIdentifiers = supportedLocales.map { locale ->
            (locale as NSLocale).localeIdentifier
        }

        print("Supported languages: $localeIdentifiers")
    }
}