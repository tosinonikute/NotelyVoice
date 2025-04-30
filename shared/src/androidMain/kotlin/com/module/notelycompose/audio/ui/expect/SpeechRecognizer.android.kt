package com.module.notelycompose.audio.ui.expect

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.RecognitionListener
import org.vosk.android.SpeechStreamService
import org.vosk.android.StorageService
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
@Serializable
data class Sentence(val text: String)
actual class SpeechRecognizer(
    private val context: Context,
) {
    private var model: Model? = null
    private var speechStreamService: SpeechStreamService? = null
    private var recognizer: Recognizer? = null
    private var isInitialized = false

    actual fun setup(){
        if (isInitialized) return
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
            },
            { exception: IOException ->
                println("Failed to unpack the model ${exception.message}")
                exception.printStackTrace()
            })
    }


    actual fun tearDown() {
            speechStreamService?.stop()
    }

    actual fun recognizeFile(filePath: String, onComplete:(String?)->Unit) {
        if (speechStreamService != null) {
            speechStreamService?.stop()
            speechStreamService = null
        }
            try {
                val ais: InputStream = FileInputStream(filePath)
                if (ais.skip(44) != 44L) throw IOException("File too short")

                speechStreamService = SpeechStreamService(recognizer, ais, 16000f)
                speechStreamService?.start(object : RecognitionListener {
                    override fun onPartialResult(hypothesis: String?) {
                        println("Partial result : $hypothesis")
                    }

                    override fun onResult(hypothesis: String?) {
                        println("result : $hypothesis")
                        onComplete(Json.decodeFromString<Sentence>(hypothesis?:"").text)
                    }

                    override fun onFinalResult(hypothesis: String?) {
                        println("Final result : $hypothesis")
                        onComplete(Json.decodeFromString<Sentence>(hypothesis?:"").text)


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