package com.module.notelycompose.audio.ui.expect

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSURL
import platform.Speech.SFSpeechRecognitionTask
import platform.Speech.SFSpeechRecognizer
import platform.Speech.SFSpeechRecognizerAuthorizationStatus
import platform.Speech.SFSpeechURLRecognitionRequest
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.resume

actual class SpeechRecognizer {
    private var recognizer:SFSpeechRecognizer? = null
    private lateinit var request : SFSpeechURLRecognitionRequest
    private var recognitionTask: SFSpeechRecognitionTask? = null

    actual fun setup() {

    }

    actual fun tearDown() {
        recognitionTask?.cancel()
        recognitionTask = null
    }

    actual fun recognizeFile(filePath: String, onComplete: (String?) -> Unit) {
        if(hasSpeechPermission()){
            startRecognizing(filePath,onComplete)
        }else {
            SFSpeechRecognizer.requestAuthorization { authorized ->
                if (authorized == SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusAuthorized) {
                    startRecognizing(filePath, onComplete)
                }

            }
        }

    }

    private fun startRecognizing(filePath: String, onComplete: (String?) -> Unit){
        recognizer = SFSpeechRecognizer()
        request = SFSpeechURLRecognitionRequest(NSURL.fileURLWithPath(filePath))
        recognitionTask = recognizer?.recognitionTaskWithRequest(request) { result, error ->
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
                        println("Final Result $text")
                        onComplete(text)
                    }
                    else -> {
                        val text = result.bestTranscription.formattedString
                        println("Result $text")
                        onComplete(text)
                    }
                }
            }
        }

    }

     private fun hasSpeechPermission(): Boolean {
        return SFSpeechRecognizer.authorizationStatus() == SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusAuthorized
    }
}