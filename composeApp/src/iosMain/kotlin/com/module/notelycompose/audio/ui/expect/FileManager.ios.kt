package com.module.notelycompose.audio.ui.expect

import platform.Foundation.*


actual fun deleteFile(filePath: String): Boolean {
    return NSFileManager.defaultManager.removeItemAtPath(filePath, null)
}

actual fun fileExists(filePath: String): Boolean {
    return NSFileManager.defaultManager.fileExistsAtPath(filePath)
}