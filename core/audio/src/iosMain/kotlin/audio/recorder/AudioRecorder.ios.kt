package audio.recorder

import audio.utils.generateNewAudioFile
import io.github.aakira.napier.Napier
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AVFAudio.AVAudioQualityHigh
import platform.AVFAudio.AVAudioRecorder
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryOptionAllowBluetooth
import platform.AVFAudio.AVAudioSessionCategoryOptionDefaultToSpeaker
import platform.AVFAudio.AVAudioSessionCategoryPlayAndRecord
import platform.AVFAudio.AVAudioSessionPortBluetoothA2DP
import platform.AVFAudio.AVAudioSessionPortBluetoothHFP
import platform.AVFAudio.AVAudioSessionPortBuiltInMic
import platform.AVFAudio.AVAudioSessionRecordPermissionGranted
import platform.AVFAudio.AVAudioSessionRouteChangeNotification
import platform.AVFAudio.AVAudioSessionRouteChangeReasonKey
import platform.AVFAudio.AVAudioSessionRouteChangeReasonNewDeviceAvailable
import platform.AVFAudio.AVAudioSessionRouteChangeReasonOldDeviceUnavailable
import platform.AVFAudio.AVEncoderAudioQualityKey
import platform.AVFAudio.AVFormatIDKey
import platform.AVFAudio.AVNumberOfChannelsKey
import platform.AVFAudio.AVSampleRateKey
import platform.AVFAudio.availableInputs
import platform.AVFAudio.setActive
import platform.CoreAudioTypes.kAudioFormatLinearPCM
import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSNumber
import platform.Foundation.NSURL
import platform.darwin.NSObjectProtocol
import kotlin.coroutines.resume

actual class AudioRecorder{

    private var audioRecorder: AVAudioRecorder? = null
    private var recordingSession: AVAudioSession = AVAudioSession.sharedInstance()
    private var recordingURL: NSURL? = null
    private var isCurrentlyPaused = false

    private var routeChangeObserver: NSObjectProtocol? = null


    /**
     * Call when entering recording screen
     */
    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun setup() {
        try {
            recordingSession.setCategory(
                AVAudioSessionCategoryPlayAndRecord,
                withOptions = AVAudioSessionCategoryOptionAllowBluetooth or AVAudioSessionCategoryOptionDefaultToSpeaker,
                null
            )
            recordingSession.setActive(true, null)

            // Observe route changes
            routeChangeObserver = NSNotificationCenter.defaultCenter.addObserverForName(
                name = AVAudioSessionRouteChangeNotification,
                `object` = null,
                queue = null
            ) { notification ->
                handleRouteChange(notification)
            }

            Napier.d { "Audio session setup done" }
        } catch (e: Exception) {
            Napier.d { "Audio session setup failed: ${e.message}" }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun selectBluetoothIfAvailable(): Boolean {
        val bluetoothInput = recordingSession.availableInputs?.firstOrNull {
            val item = it as? platform.AVFAudio.AVAudioSessionPortDescription
            item?.portType == AVAudioSessionPortBluetoothHFP || item?.portType == AVAudioSessionPortBluetoothA2DP
        } as? platform.AVFAudio.AVAudioSessionPortDescription

        return if (bluetoothInput != null) {
            recordingSession.setPreferredInput(bluetoothInput, null)
            Napier.d { "Bluetooth mic selected: ${bluetoothInput.portName}" }
            true
        } else {
            Napier.d { "No Bluetooth mic found" }
            false
        }
    }

    /**
     * Handles Bluetooth disconnection or route changes
     */
    private fun handleRouteChange(notification: NSNotification?) {
        val reason = notification?.userInfo?.get(AVAudioSessionRouteChangeReasonKey) as? NSNumber
        val reasonValue = reason?.integerValue ?: -1

        when (reasonValue.toULong()) {
            AVAudioSessionRouteChangeReasonNewDeviceAvailable -> {
                Napier.d { "New audio device available" }
            }
            AVAudioSessionRouteChangeReasonOldDeviceUnavailable -> {
                Napier.d { "Bluetooth device disconnected" }
                // Automatically fallback to built-in mic
                selectBuiltInMic()
            }
            else -> {
                Napier.d { "Route changed (reason: $reasonValue)" }
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun selectBuiltInMic() {
        val builtIn = recordingSession.availableInputs?.firstOrNull {
            val item = it as? platform.AVFAudio.AVAudioSessionPortDescription
            item?.portType == AVAudioSessionPortBuiltInMic
        } as?  platform.AVFAudio.AVAudioSessionPortDescription
        if (builtIn != null) {
            recordingSession.setPreferredInput(builtIn, null)
            Napier.d { "Fell back to built-in mic ${builtIn.portName}" }
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
            routeChangeObserver?.let {
                NSNotificationCenter.defaultCenter.removeObserver(it)
                routeChangeObserver = null
            }
            recordingSession.setActive(false, null)
        } catch (e: Exception) {
            Napier.d { "Audio session teardown failed: ${e.message}" }
        }
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual fun startRecording(
        useBluetoothMic: Boolean
    ) {
        // 1. Request permissions early
        if (!hasRecordingPermission()) {
            Napier.d { "Recording permission not granted" }
            return
        }

        if(useBluetoothMic) {
            // Try selecting Bluetooth first
            val usingBluetooth = selectBluetoothIfAvailable()
            if (!usingBluetooth) {
                selectBuiltInMic()
            }
        }else{
            selectBuiltInMic()
        }

        this.recordingURL = generateNewAudioFile() ?: run {
            Napier.e { "Failed to create recording URL" }
            return
        }

        val settings = mapOf<Any?, Any?>(
            AVFormatIDKey to kAudioFormatLinearPCM,
            AVSampleRateKey to 16000.0,
            AVNumberOfChannelsKey to 1,
            AVEncoderAudioQualityKey to AVAudioQualityHigh,
        )
        audioRecorder = AVAudioRecorder(recordingURL!!, settings, null)
        if (audioRecorder?.prepareToRecord() == true) {
            val isRecording = audioRecorder?.record()
            isCurrentlyPaused = false
            Napier.d { "Recording started successfully $isRecording" }
        } else {
            Napier.d { "Failed to prepare recording" }
            audioRecorder = null
        }

    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun stopRecording() {
        audioRecorder?.let { recorder ->
            if (recorder.isRecording()) {
                recorder.stop()
            }
        }

        audioRecorder = null
        isCurrentlyPaused = false
    }

    actual fun isRecording(): Boolean {
        return audioRecorder?.isRecording() ?: false
    }

    actual fun hasRecordingPermission(): Boolean {
        return recordingSession.recordPermission() == AVAudioSessionRecordPermissionGranted
    }

    actual suspend fun requestRecordingPermission(): Boolean {
        if (hasRecordingPermission()) return true

        return suspendCancellableCoroutine { continuation ->
            recordingSession.requestRecordPermission { granted ->
                continuation.resume(granted)
            }
        }
    }

    actual fun getRecordingFilePath(): String {
        return recordingURL?.path.orEmpty()
    }

    actual fun pauseRecording() {
        if (isRecording() && !isCurrentlyPaused) {
            audioRecorder?.let { recorder ->
                recorder.pause()
                isCurrentlyPaused = true
                Napier.d { "Recording paused successfully" }
            }
        }
    }

    actual fun resumeRecording() {
        if (isCurrentlyPaused) {
            audioRecorder?.let { recorder ->
                recorder.record()
                isCurrentlyPaused = false
                Napier.d { "Recording resumed successfully" }
            }
        }
    }

    actual fun isPaused(): Boolean {
        return isCurrentlyPaused
    }
}