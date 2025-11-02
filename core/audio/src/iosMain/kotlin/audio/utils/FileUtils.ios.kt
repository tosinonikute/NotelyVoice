package audio.utils

import io.github.aakira.napier.Napier
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.free
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import kotlin.random.Random

internal fun generateNewAudioFile(prefix: String = RECORDING_PREFIX): NSURL? {
    val randomNumber = Random.nextInt(100000, 999999)
    val fileName = "$prefix${randomNumber}$RECORDING_EXTENSION"
    val documentsDirectory = NSFileManager.defaultManager.URLsForDirectory(
        NSDocumentDirectory,
        NSUserDomainMask
    ).first() as NSURL

    return documentsDirectory.URLByAppendingPathComponent(fileName)
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal fun NSURL.savePickedAudioToAppStorage(): NSURL?{
    val file = generateNewAudioFile() ?: return null
    val fileManager = NSFileManager.defaultManager

    val errorPtr = nativeHeap.alloc<ObjCObjectVar<NSError?>>()
    errorPtr.value = null

    val success = fileManager.copyItemAtURL(this, file, errorPtr.ptr)

    if(!success){
        Napier.e { "File copy failed: ${errorPtr.value?.localizedDescription}" }
        nativeHeap.free(errorPtr)
        return null
    }
    nativeHeap.free(errorPtr)
    return file
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual fun deleteFile(filePath: String): Boolean {
    if (filePath.isBlank()) {
        Napier.e { "Cannot delete file: filePath is null or empty" }
        return false
    }

    memScoped {
        val error: ObjCObjectVar<NSError?> = alloc()
        val success = NSFileManager.defaultManager.removeItemAtPath(filePath, error.ptr)

        if (!success) {
            Napier.e{"Failed to delete file at path: $filePath"}
            Napier.e{"Error: ${error.value?.localizedDescription}"}
        } else {
            Napier.e{"File deleted successfully at path: $filePath"}
        }

        return success
    }
}

actual fun fileExists(filePath: String): Boolean {
    return NSFileManager.defaultManager.fileExistsAtPath(filePath)
}