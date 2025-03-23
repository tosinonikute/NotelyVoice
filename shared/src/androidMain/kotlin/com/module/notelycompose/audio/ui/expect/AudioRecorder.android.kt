package com.module.notelycompose.audio.ui.expect

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume
import kotlin.random.Random

private const val DEFAULT = "recording.mp3"
private const val RECORDING_PREFIX = "recording_"
private const val RECORDING_EXTENSION = ".mp3"

actual class AudioRecorder(
    private val context: Context,
    private val permissionLauncher: ActivityResultLauncher<String>?
) {
    private var recorder: MediaRecorder? = null
    private var isCurrentlyRecording = false
    private var permissionContinuation: ((Boolean) -> Unit)? = null
    private var currentRecordingPath: String? = null

    actual fun startRecording() {
        val randomNumber = Random.nextInt(100000, 999999)
        val fileName = "$RECORDING_PREFIX$randomNumber$RECORDING_EXTENSION"
        val file = File(context.cacheDir, fileName)
        currentRecordingPath = file.absolutePath

        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(file.absolutePath)

            try {
                prepare()
                start()
                isCurrentlyRecording = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    actual fun stopRecording() {
        try {
            recorder?.apply {
                stop()
                reset()
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            recorder = null
            isCurrentlyRecording = false
        }
    }

    actual fun isRecording(): Boolean {
        return isCurrentlyRecording
    }

    actual fun hasRecordingPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    actual suspend fun requestRecordingPermission() {
        if (hasRecordingPermission()) {
            return
        }

        return suspendCancellableCoroutine { continuation ->
            permissionContinuation = { isGranted ->
                continuation.resume(Unit)
            }

            if (permissionLauncher != null) {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            } else {
                continuation.resume(Unit)
            }

            continuation.invokeOnCancellation {
                permissionContinuation = null
            }
        }
    }

    actual fun getRecordingFilePath(): String {
        return currentRecordingPath ?: File(context.cacheDir, DEFAULT).absolutePath
    }
}
