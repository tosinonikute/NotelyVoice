package com.module.notelycompose.audio.ui.expect

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.RecognitionListener
import org.vosk.android.SpeechService
import org.vosk.android.StorageService
import java.io.IOException
import kotlin.coroutines.resume

@Serializable
data class Sentence(val partial: String="", val text: String="")
actual class SpeechRecognizer(
    private val context: Context,
    private val permissionLauncher: ActivityResultLauncher<String>?
) {
    private var model: Model? = null
    private var speechService: SpeechService? = null
    private var recognizer: Recognizer? = null
    private var isInitialized = false
    private var isListening = false
    private var permissionContinuation: ((Boolean) -> Unit)? = null
    actual fun hasRecordingPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }


    actual suspend fun requestRecordingPermission():Boolean {
        if (hasRecordingPermission()) {
            return true
        }

        return suspendCancellableCoroutine { continuation ->
            permissionContinuation = { isGranted ->
                continuation.resume(isGranted)
            }

            if (permissionLauncher != null) {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            } else {
                continuation.resume(false)
            }

            continuation.invokeOnCancellation {
                permissionContinuation = null
            }
        }
    }


    actual fun initialize() {
        if (isInitialized) return
        println("speech: initialize model")
        initModel(context)
    }

    private fun initModel(context: Context) {

        StorageService.unpack(
            context, "model-en-us", "model",
            { model: Model ->
                this.model = model
                recognizer = Recognizer(
                    model, 16000f
                )
                isInitialized = true
                println("speech:Recognizer initialized")
            },
            { exception: IOException ->
                println("Failed to unpack the model ${exception.message}")
                exception.printStackTrace()
            })
    }


    actual fun stopRecognizer() {
        isListening = false
        speechService?.stop()
    }

    actual fun finishRecognizer() {
        isInitialized = false
        isListening = false
        speechService?.stop()
        speechService = null
    }

    actual fun startRecognizing(
        onComplete: (Boolean, String?) -> Unit
    ) {
        if (speechService != null) {
            speechService?.stop()
            speechService = null
        }
            try {
                speechService = SpeechService(recognizer, 16000f)
                isListening = true
                println("speech:Start listening")
                speechService?.startListening(object : RecognitionListener {
                    override fun onPartialResult(hypothesis: String?) {
                        println("Partial result : $hypothesis")
                        val newText = Json.decodeFromString<Sentence>(hypothesis ?: "").partial
                        if (newText.isNotBlank())
                            onComplete(false, newText)
                    }

                    override fun onResult(hypothesis: String?) {
                        println("result : $hypothesis")
                        val newText = Json.decodeFromString<Sentence>(hypothesis ?: "").text
                        if (newText.isNotBlank())
                            onComplete(true, newText)
                    }

                    override fun onFinalResult(hypothesis: String?) {
                        println("Final result : $hypothesis")
                        //onComplete(Json.decodeFromString<Sentence>(hypothesis?:"").text)


                    }

                    override fun onError(exception: Exception?) {
                        exception?.printStackTrace()
                        println("Error : ${exception?.message}")
                    }

                    override fun onTimeout() {
                        println("TimeOut ")
                    }
                })
            } catch (e: IOException) {
                e.printStackTrace()
            }
    }
}