package audio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat
import audio.converter.AudioConverter
import audio.utils.LauncherHolder
import audio.utils.deleteFile
import audio.utils.savePickedAudioToAppStorage
import audio.utils.savePickedVideoToAppStorage

internal class AndroidFileManager(
    private val context: Context,
    private val launcherHolder: LauncherHolder,
    private val audioConverter: AudioConverter
) : FileManager {

    private var pickedAudioUri: Uri? = null
    private var pickedVideoUri: Uri? = null

    override fun launchAudioPicker(onResult: () -> Unit) {
        pickedAudioUri = null

        if (hasStoragePermissions()) {
            launcherHolder.audioPickerLauncher?.launch { uri ->
                pickedAudioUri = uri
                uri?.let { onResult() }
            }
        }
    }

    override fun launchVideoPicker(onResult: () -> Unit) {
        pickedVideoUri = null

        if (hasStoragePermissions()) {
            launcherHolder.videoPickerLauncher?.launch { uri ->
                pickedVideoUri = uri
                uri?.let { onResult() }
            }
        }
    }

    override suspend fun processPickedAudioToWav(onProgress: (Float) -> Unit): String? {
        val inputPath = copyAudioToAppStorage() ?: return null
        val outputPath = audioConverter.convertAudioToWav(inputPath, onProgress)
        deleteFile(inputPath)
        return outputPath
    }

    override suspend fun processPickedVideoToWav(onProgress: (Float) -> Unit): String? {
        val inputPath = copyVideoToAppStorage() ?: return null
        val outputPath = audioConverter.extractAudioFromVideoToWav(inputPath, onProgress)
        deleteFile(inputPath)
        return outputPath
    }

    private fun copyAudioToAppStorage(): String? {
        return pickedAudioUri?.let { context.savePickedAudioToAppStorage(it)?.absolutePath }
            .also { pickedAudioUri = null }
    }

    private fun copyVideoToAppStorage(): String? {
        return pickedVideoUri?.let { context.savePickedVideoToAppStorage(it)?.absolutePath }
            .also { pickedVideoUri = null }
    }

    private fun hasStoragePermissions(): Boolean {
        val requiredPermissions = mutableListOf<String>().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.READ_MEDIA_AUDIO)
                add(Manifest.permission.READ_MEDIA_VIDEO)
            } else {
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        val granted = requiredPermissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        if (!granted) {
            launcherHolder.permissionLauncher?.launch(requiredPermissions.toTypedArray())
        }

        return granted
    }
}