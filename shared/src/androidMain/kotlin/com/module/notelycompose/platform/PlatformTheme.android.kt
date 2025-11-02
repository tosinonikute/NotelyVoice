package com.module.notelycompose.platform

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.net.toUri
import com.module.notelycompose.FileSaverHandler
import com.module.notelycompose.platform.pdf.AndroidPdfGenerator

actual class PlatformUtils(
    private val context: Context,
    private val fileSaverHandler: FileSaverHandler,
    private val androidPdfGenerator: AndroidPdfGenerator
) {

    actual fun shareText(text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        val chooser = Intent.createChooser(intent, "Share via")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    actual fun shareRecording(path: String) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            File(path)
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "audio/wav"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(shareIntent, "Share WAV file")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    actual fun exportRecordingWithFilePicker(
        sourcePath: String,
        fileName: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        try {
            val sourceFile = File(sourcePath)
            if (!sourceFile.exists()) {
                onResult(false, "Source file not found")
                return
            }

            fileSaverHandler.saveFile(fileName) { targetUri ->
                try {
                    val success = exportRecordingWithStorageAccessFramework(
                        sourcePath = sourcePath,
                        targetUri = targetUri
                    )
                    onResult(success, if (success) "File exported successfully" else "Failed to export file")
                } catch (e: Exception) {
                    onResult(false, "Export failed: ${e.message}")
                }
            }
        } catch (e: Exception) {
            onResult(false, "Export failed: ${e.message}")
        }
    }

    actual fun requestStoragePermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return true // No permission needed for Android 10+
        }

        return context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    actual fun exportTextWithFilePicker(
        text: String,
        fileName: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        try {
            fileSaverHandler.saveFile(fileName) { targetUri ->
                try {
                    val success = exportTextWithStorageAccessFramework(
                        text = text,
                        targetUri = targetUri
                    )
                    onResult(success, if (success) "Text file exported successfully" else "Failed to export text file")
                } catch (e: Exception) {
                    onResult(false, "Export failed: ${e.message}")
                }
            }
        } catch (e: Exception) {
            onResult(false, "Export failed: ${e.message}")
        }
    }

    actual fun exportTextAsPDFWithFilePicker(
        text: String,
        fileName: String,
        textSize: Float,
        onResult: (Boolean, String?) -> Unit
    ) {
        try {
            val pdfFileName = if (fileName.endsWith(".pdf")) fileName else "$fileName.pdf"

            fileSaverHandler.saveFile(pdfFileName) { targetUri ->
                try {
                    val success = androidPdfGenerator.createPdfDocument(text, targetUri, textSize)
                    onResult(success, if (success) "PDF exported successfully" else "Failed to export PDF")
                } catch (e: Exception) {
                    onResult(false, "PDF export failed: ${e.message}")
                }
            }
        } catch (e: Exception) {
            onResult(false, "PDF export failed: ${e.message}")
        }
    }

    // Private functions:

    private fun exportRecordingWithStorageAccessFramework(
        sourcePath: String,
        targetUri: String
    ): Boolean {
        return try {
            val sourceFile = File(sourcePath)
            if (!sourceFile.exists()) {
                return false
            }

            val uri = targetUri.toUri()
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                sourceFile.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun exportTextWithStorageAccessFramework(
        text: String,
        targetUri: String
    ): Boolean {
        return try {
            val uri = targetUri.toUri()
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(text.toByteArray(Charsets.UTF_8))
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    actual fun copyTextToClipboard(text: String, onResult: (Boolean, String?) -> Unit) {
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("text", text)
            clipboard.setPrimaryClip(clip)
            onResult(true, "Text copied to clipboard")
        } catch (e: Exception) {
            onResult(false, "Failed to copy: ${e.message}")
        }
    }
}
