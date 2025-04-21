package com.module.notelycompose.audio.ui.expect

import java.io.File


actual fun deleteFile(filePath: String): Boolean {
    println("Deleting file: $filePath")
    return try {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        } else {
            false
        }
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

actual fun fileExists(filePath: String): Boolean {
    return File(filePath).exists()
}