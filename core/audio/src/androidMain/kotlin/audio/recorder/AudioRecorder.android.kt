package audio.recorder

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import androidx.core.content.ContextCompat
import audio.utils.LauncherHolder
import audio.utils.generateWavFile
import com.github.squti.androidwaverecorder.WaveRecorder
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume

private const val DEFAULT = "recording.wav"

private var selectedEncoding = AudioFormat.ENCODING_PCM_16BIT

actual class AudioRecorder(
    private val context: Context,
    private val launcherHolder: LauncherHolder
) {
    private var recorder: WaveRecorder? = null
    private var isCurrentlyRecording = false
    private var isCurrentlyPaused = false
    private var permissionContinuation: ((Boolean) -> Unit)? = null
    private var currentRecordingPath: String? = null

    actual  fun startRecording(useBluetoothMic: Boolean) {
        val file = context.generateWavFile()
        currentRecordingPath = file.absolutePath

        recorder = WaveRecorder(file.absolutePath)
            .configureWaveSettings {
                sampleRate = 16000
                channels = AudioFormat.CHANNEL_IN_MONO
                audioEncoding = selectedEncoding
            }.configureSilenceDetection {
                minAmplitudeThreshold = 2000
                bufferDurationInMillis = 1500
                preSilenceDurationInMillis = 1500
            }

        try {
            recorder?.startRecording()
            isCurrentlyRecording = true
            isCurrentlyPaused = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    actual   fun stopRecording() {
        try {
            recorder?.stopRecording()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            recorder = null
            isCurrentlyRecording = false
            isCurrentlyPaused = false
        }
    }

    actual   fun isRecording(): Boolean {
        return isCurrentlyRecording
    }

    actual   fun hasRecordingPermission(): Boolean {
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

            if (launcherHolder.permissionLauncher != null) {
                launcherHolder.permissionLauncher?.launch(arrayOf(Manifest.permission.RECORD_AUDIO))
            } else {
                continuation.resume(false)
            }

            continuation.invokeOnCancellation {
                permissionContinuation = null
            }
        }
    }

    actual   fun getRecordingFilePath(): String {
        return currentRecordingPath ?: File(context.cacheDir, DEFAULT).absolutePath
    }

    actual  suspend fun setup() {
    }

    actual suspend fun teardown() {
    }

    actual fun pauseRecording() {
        if (isCurrentlyRecording && !isCurrentlyPaused) {
            try {
                recorder?.pauseRecording()
                isCurrentlyPaused = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    actual fun resumeRecording() {
        if (isCurrentlyRecording && isCurrentlyPaused) {
            try {
                recorder?.resumeRecording()
                isCurrentlyPaused = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    actual fun isPaused(): Boolean {
        return isCurrentlyPaused
    }
}