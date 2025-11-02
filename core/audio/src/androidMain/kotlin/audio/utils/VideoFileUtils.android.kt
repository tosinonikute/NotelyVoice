package audio.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream

internal const val VIDEO_RECORDING_PREFIX = "video_recording_"
internal const val VIDEO_IMPORTING_PREFIX = "video_importing_"
internal const val DEFAULT_VIDEO_EXTENSION = ".mp4"

fun Context.getVideoExtensionFromUri(uri: Uri): String {
    val mimeType = contentResolver.getType(uri)
    return when (mimeType) {
        "video/mp4" -> ".mp4"
        "video/quicktime" -> ".mov"
        "video/x-msvideo" -> ".avi"
        "video/x-matroska" -> ".mkv"
        "video/webm" -> ".webm"
        "video/3gpp" -> ".3gp"
        else -> {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameIndex != -1) {
                        val displayName = cursor.getString(displayNameIndex)
                        val extension = displayName?.substringAfterLast('.', "")
                        if (!extension.isNullOrEmpty()) {
                            return when (extension.lowercase()) {
                                "mp4", "mov", "avi", "mkv", "webm", "3gp" -> ".$extension"
                                else -> DEFAULT_VIDEO_EXTENSION
                            }
                        }
                    }
                }
            }
            DEFAULT_VIDEO_EXTENSION
        }
    }
}

fun Context.generateVideoFile(prefix: String = VIDEO_RECORDING_PREFIX, extension: String = DEFAULT_VIDEO_EXTENSION): File {
    val fileName = "$prefix${System.currentTimeMillis()}$extension"
    val outputFile = File(this.getExternalFilesDir(Environment.DIRECTORY_MOVIES), fileName)
    return outputFile
}

fun Context.savePickedVideoToAppStorage(uri: Uri): File? {
    val extension = getVideoExtensionFromUri(uri)
    val file = generateVideoFile(prefix = VIDEO_IMPORTING_PREFIX, extension = extension)
    return try {
        this.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
