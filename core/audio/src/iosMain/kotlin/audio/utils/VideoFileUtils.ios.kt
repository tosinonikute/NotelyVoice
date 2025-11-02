package audio.utils

import io.github.aakira.napier.Napier
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.free
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import kotlin.random.Random

internal const val VIDEO_RECORDING_PREFIX = "video_recording_"
internal const val VIDEO_IMPORTING_PREFIX = "video_importing_"
internal const val DEFAULT_VIDEO_EXTENSION = ".mp4"

@OptIn(ExperimentalForeignApi::class)
internal fun NSURL.getVideoExtensionFromUrl(): String {
    val pathExt = this.pathExtension
    if (!pathExt.isNullOrEmpty()) {
        val extension = ".$pathExt"
        // Validate it's a known video extension
        return when (pathExt.lowercase()) {
            "mp4", "mov", "avi", "mkv", "webm", "3gp", "m4v" -> extension
            else -> DEFAULT_VIDEO_EXTENSION
        }
    }
    return DEFAULT_VIDEO_EXTENSION
}

internal fun generateNewVideoFile(
    prefix: String = VIDEO_RECORDING_PREFIX,
    extension: String = DEFAULT_VIDEO_EXTENSION
): NSURL? {
    val randomNumber = Random.nextInt(100000, 999999)
    val fileName = "$prefix$randomNumber$extension"
    val documentsDirectory = NSFileManager.defaultManager.URLsForDirectory(
        NSDocumentDirectory,
        NSUserDomainMask
    ).first() as NSURL

    return documentsDirectory.URLByAppendingPathComponent(fileName)
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal fun NSURL.savePickedVideoToAppStorage(): NSURL? {
    val extension = this.getVideoExtensionFromUrl()
    val file = generateNewVideoFile(prefix = VIDEO_IMPORTING_PREFIX, extension = extension)
        ?: return null

    val fileManager = NSFileManager.defaultManager
    val errorPtr = nativeHeap.alloc<ObjCObjectVar<NSError?>>()
    errorPtr.value = null

    val success = fileManager.copyItemAtURL(this, file, errorPtr.ptr)

    if (!success) {
        Napier.e { "Video file copy failed: ${errorPtr.value?.localizedDescription}" }
        nativeHeap.free(errorPtr)
        return null
    }
    nativeHeap.free(errorPtr)
    return file
}
