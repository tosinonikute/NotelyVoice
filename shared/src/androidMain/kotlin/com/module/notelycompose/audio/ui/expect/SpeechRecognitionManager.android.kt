package com.module.notelycompose.audio.ui.expect

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognitionService
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONArray
import org.json.JSONObject

actual class SpeechRecognitionManager(
    private val context: Context,
    private val permissionLauncher: ActivityResultLauncher<String>?,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {


    companion object {
        private const val TAG = "SpeechToText"
        private const val MIN_SDK_FOR_SPEECH_SUPPORT = 21
        private const val BROKEN_STOP_SDK = 29
        private const val MIN_SDK_FOR_ON_DEVICE_SPEECH_SUPPORT = 31
        private const val MISSING_CONFIDENCE = -1.0
        private const val SPEECH_THRESHOLD_RMS = 9
    }

    private var permissionContinuation: ((Boolean) -> Unit)? = null
    private var initializedSuccessfully = false
    private var permissionToRecordAudio = false
    private var listening = false
    private var alwaysUseStop = false
    private var intentLookup = false
    private var resultSent = false
    private var lastOnDevice = false

    private var speechRecognizer: SpeechRecognizer? = null
    private var recognizerIntent: Intent? = null
    private var previousPartialResults = false
    private var lastFinalTime: Long = 0
    private var speechStartTime: Long = 0
    private var minRms = 1000.0f
    private var maxRms = -100.0f
    private lateinit var onTextRecognized: (String, Boolean) -> Unit
    private lateinit var onStatusChanged: (String) -> Unit
    private lateinit var onError: (String) -> Unit
    private lateinit var onSoundLevelChanged: (Float) -> Unit


    actual fun hasSpeechPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    actual suspend fun requestPermissions(doOnGranted: () -> Unit) {
        if (hasSpeechPermission()) {
            return
        }

        return suspendCancellableCoroutine { continuation ->
            permissionContinuation = { isGranted ->
                if (isGranted) doOnGranted()
            }

            permissionLauncher?.launch(Manifest.permission.RECORD_AUDIO)

            continuation.invokeOnCancellation {
                permissionContinuation = null
            }
        }
    }

    actual fun initializeRecognizer(
        onTextRecognized: (String, Boolean) -> Unit,
        onStatusChanged: (String) -> Unit,
        onError: (String) -> Unit,
        onSoundLevelChanged: (Float) -> Unit
    ) {
        this.onTextRecognized = onTextRecognized
        this.onStatusChanged = onStatusChanged
        this.onError = onError
        this.onSoundLevelChanged = onSoundLevelChanged

        if (sdkVersionTooLow()) {
            onError("SDK version too low")
            return
        }
        println("Start initialize")
        permissionToRecordAudio = hasSpeechPermission()

        println("Checked permission")

        if (!permissionToRecordAudio) {
            scope.launch {
                requestPermissions {
                    permissionToRecordAudio = true
                    completeInitialize()
                }
            }

        } else {
            println("has permission, completing")
            completeInitialize()
        }
        println("leaving initializeIfPermitted")

    }

    private fun completeInitialize() {
        println("completeInitialize")
        if (permissionToRecordAudio) {
            println("Testing recognition availability")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!SpeechRecognizer.isRecognitionAvailable(context) &&
                    !SpeechRecognizer.isOnDeviceRecognitionAvailable(context)
                ) {
                    Log.e(TAG, "Speech recognition not available on this device")
                        onError("Speech recognition not available on this device")
                    return
                }
            } else {
                if (!SpeechRecognizer.isRecognitionAvailable(context)) {
                    Log.e(TAG, "Speech recognition not available on this device")

                        onError("Speech recognition not available on this device")
                    return
                }
            }
        }

        initializedSuccessfully = permissionToRecordAudio
        println("sending result")
        onStatusChanged(if (initializedSuccessfully) "initialized" else "failed")
    }

    actual fun startRecognition() {
        startListening(partialResults = true, onDevice = true)
    }

    actual fun stopRecognition() {
        stopListening()
    }

    private fun startListening(
        partialResults: Boolean,
        onDevice: Boolean
    ) {
        if (sdkVersionTooLow() || !initializedSuccessfully || listening) {
            onError("Not initialized or already listening")
            return
        }

        resultSent = false
        createRecognizer(onDevice)
        minRms = 1000.0f
        maxRms = -100.0f
        println("Start listening")


        setupRecognizerIntent(partialResults, onDevice)
            speechRecognizer?.startListening(recognizerIntent)

        speechStartTime = System.currentTimeMillis()
        notifyListening(true)
        println("Start listening done")
    }

    private fun stopListening() {
        if (sdkVersionTooLow() || !initializedSuccessfully || !listening) {
            onError("Not initialized or not listening")
            return
        }
        notifyListening(false)
        println("Stop listening")
            speechRecognizer?.stopListening()

        //   if (!(Build.VERSION.SDK_INT != BROKEN_STOP_SDK || alwaysUseStop)) {
        //     destroyRecognizer()
        //}
        println("Stop listening done")
    }

    private fun cancelListening() {
        if (sdkVersionTooLow() || !initializedSuccessfully || !listening) {
            onError("Not initialized or not listening")
            return
        }

        println("Cancel listening")
            speechRecognizer?.cancel()

        if (!(Build.VERSION.SDK_INT != BROKEN_STOP_SDK || alwaysUseStop)) {
            destroyRecognizer()
        }

        notifyListening(false)
        println("Cancel listening done")
    }


    actual fun destroy() {
        destroyRecognizer()
    }

    private fun notifyListening(isRecording: Boolean) {
        if (listening == isRecording) return
        listening = isRecording

        val status = if (isRecording) "listening" else "notListening"
        println("Notify status:$status")
        onStatusChanged(status)

        if (!isRecording) {
            val doneStatus = if (resultSent) "done" else "doneNoResult"
            println("Notify status:$doneStatus")
            onStatusChanged(doneStatus)
        }
    }

    private fun updateResults(speechBundle: Bundle?, isFinal: Boolean) {
        if (isDuplicateFinal(isFinal)) {
            println("Discarding duplicate final")
            return
        }

        val userSaid = speechBundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (!userSaid.isNullOrEmpty()) {
            try {
                val speechResult = JSONObject().apply {
                    put("finalResult", isFinal)
                    val confidence = speechBundle.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)
                    val alternates = JSONArray()

                    userSaid.forEachIndexed { resultIndex, result ->
                        println("Calling results callback")
                        resultSent = true
                        onTextRecognized(result, isFinal)
//                        JSONObject().apply {
//                            put("recognizedWords", result)
//                            put(
//                                "confidence",
//                                confidence?.getOrNull(resultIndex) ?: MISSING_CONFIDENCE
//                            )
//                        }.let { alternates.put(it) }
                    }

                    put("alternates", alternates)
                }


            } catch (e: Exception) {
                Log.e(TAG, "Error creating JSON result", e)
            }
        } else {
            println("Results null or empty")
        }
    }

    private fun Context.findComponentName(): ComponentName? {
        val list: List<ResolveInfo> = packageManager.queryIntentServices(
            Intent(RecognitionService.SERVICE_INTERFACE),
            0
        )
        println("RecognitionService, found: ${list.size}")
        list.forEach {
            it.serviceInfo?.let { info ->
                println("RecognitionService: packageName: ${info.packageName}, name: ${info.name}")
            }
        }
        return list.firstOrNull()?.serviceInfo?.let {
            ComponentName(it.packageName, it.name)
        }
    }

    private fun createRecognizer(onDevice: Boolean) {
        if (speechRecognizer != null && onDevice == lastOnDevice) {
            return
        }

        lastOnDevice = onDevice
        speechRecognizer?.destroy()
        speechRecognizer = null
        println("Creating recognizer")
        speechRecognizer = if (intentLookup) {
                SpeechRecognizer.createSpeechRecognizer(
                    context,
                    context.findComponentName()
                ).apply {
                    println("Setting listener after intent lookup")
                    setRecognitionListener(recognizerListener)
                }
            } else {
                val supportsLocal =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && onDevice) {
                        SpeechRecognizer.isOnDeviceRecognitionAvailable(context)
                    } else {
                        false
                    }

                if (supportsLocal && Build.VERSION.SDK_INT >= MIN_SDK_FOR_ON_DEVICE_SPEECH_SUPPORT) {
                    SpeechRecognizer.createOnDeviceSpeechRecognizer(context).apply {
                        println("Setting on device listener")
                        setRecognitionListener(recognizerListener)
                    }
                } else {
                    SpeechRecognizer.createSpeechRecognizer(context).apply {
                        println("Setting default listener")
                        setRecognitionListener(recognizerListener)
                    }
                }
            }

            if (speechRecognizer == null) {
                Log.e(TAG, "Speech recognizer null")
                onError("Speech recognizer null")
            }


        println("before setup intent")
        setupRecognizerIntent(true, false)
        println("after setup intent")
    }

    private fun setupRecognizerIntent(
        partialResults: Boolean,
        onDevice: Boolean
    ) {
        println("setupRecognizerIntent")
        if (partialResults != previousPartialResults) {
            previousPartialResults = partialResults
                recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    println("In RecognizerIntent apply")
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                   // putExtra(
                     //   RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
                       // 10000L
                    //)
                    println("put model")
                    putExtra(
                        RecognizerIntent.EXTRA_CALLING_PACKAGE,
                        context.applicationInfo.packageName
                    )
                    println("put package")
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, partialResults)
                    println("put partial")
                    if (onDevice) {
                        putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, onDevice)
                    }
                    putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10)
                }
        }
    }

    private fun isDuplicateFinal(isFinal: Boolean): Boolean {
        if (!isFinal) return false
        val delta = System.currentTimeMillis() - lastFinalTime
        lastFinalTime = System.currentTimeMillis()
        return delta in 0..99
    }

    private fun destroyRecognizer() {
            println("Recognizer destroy")
            speechRecognizer?.destroy()
            speechRecognizer = null
    }

    private fun sdkVersionTooLow() = Build.VERSION.SDK_INT < MIN_SDK_FOR_SPEECH_SUPPORT

    private fun sendError(errorMsg: String) {
        try {
            JSONObject().apply {
                put("errorMsg", errorMsg)
                put("permanent", true)
            }.also { speechError ->
                    onError(speechError.toString())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error creating error JSON", e)
        }
    }

    private val recognizerListener = object : RecognitionListener {
        override fun onPartialResults(partialResults: Bundle?) {
            updateResults(partialResults, false)
            println("Partial : $partialResults")
        }

        override fun onResults(results: Bundle?) {
            updateResults(results, true)
            println("Partial : $results")
        }

        override fun onEndOfSpeech() = notifyListening(false)

        override fun onError(errorCode: Int) {
            val delta = System.currentTimeMillis() - speechStartTime
            val errorReturn =
                if (errorCode == SpeechRecognizer.ERROR_NO_MATCH && maxRms < SPEECH_THRESHOLD_RMS) {
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT
                } else {
                    errorCode
                }

            println("Error $errorCode after start at $delta $minRms / $maxRms")

            val errorMsg = when (errorReturn) {
                SpeechRecognizer.ERROR_AUDIO -> "error_audio_error"
                SpeechRecognizer.ERROR_CLIENT -> "error_client"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "error_permission"
                SpeechRecognizer.ERROR_NETWORK -> "error_network"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "error_network_timeout"
                SpeechRecognizer.ERROR_NO_MATCH -> "error_no_match"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "error_busy"
                SpeechRecognizer.ERROR_SERVER -> "error_server"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "error_speech_timeout"
                SpeechRecognizer.ERROR_LANGUAGE_NOT_SUPPORTED -> "error_language_not_supported"
                SpeechRecognizer.ERROR_LANGUAGE_UNAVAILABLE -> "error_language_unavailable"
                SpeechRecognizer.ERROR_SERVER_DISCONNECTED -> "error_server_disconnected"
                SpeechRecognizer.ERROR_TOO_MANY_REQUESTS -> "error_too_many_requests"
                else -> "error_unknown ($errorCode)"
            }

            sendError(errorMsg)
            if (listening) {
                notifyListening(false)
            }
        }

        override fun onRmsChanged(rmsdB: Float) {

        }

        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
        override fun onBeginningOfSpeech() {}

    }


}