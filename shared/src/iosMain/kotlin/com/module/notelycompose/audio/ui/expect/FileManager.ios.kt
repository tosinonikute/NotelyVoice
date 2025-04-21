package com.module.notelycompose.audio.ui.expect

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.*


@OptIn(ExperimentalForeignApi::class)
actual fun deleteFile(filePath: String): Boolean {
    memScoped {
        val error: ObjCObjectVar<NSError?> = alloc()
        val success = NSFileManager.defaultManager.removeItemAtPath(filePath, error.ptr)

        if (!success) {
            println("Failed to delete file at path: $filePath")
            println("Error: ${error.value?.localizedDescription}")
        } else {
            println("File deleted successfully at path: $filePath")
        }

        return success
    }
}

actual fun fileExists(filePath: String): Boolean {
    return NSFileManager.defaultManager.fileExistsAtPath(filePath)
}