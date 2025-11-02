package com.module.notelycompose.export.domain

import kotlinx.cinterop.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.Foundation.*
import platform.UIKit.*
import platform.darwin.NSObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

private const val TEXT_BLANK_DEFAULT = "no-title"
private const val PATTERN_DATE_FORMAT = "yyyy-MM-ddHH-mm-ss-SSS"

class ExportSelectionInteractorImpl : ExportSelectionInteractor {

    // Store delegate reference to prevent garbage collection
    private var delegateRef: DocumentPickerDelegate? = null

    @OptIn(DelicateCoroutinesApi::class)
    override fun exportAllSelection(
        texts: List<String>,
        titles: List<String>,
        audioPath: List<String>,
        shouldExportAudio: Boolean,
        shouldExportTxt: Boolean,
        onProgress: (Float) -> Unit,
        onResult: (Result<String>) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val folderUrl = pickFolder()

            if (folderUrl == null) {
                onResult(Result.failure(NoFolderSelectedException("Folder selection cancelled")))
                return@launch
            }

            val result = withContext(Dispatchers.IO) {
                performExport(
                    folderUrl,
                    texts,
                    titles,
                    audioPath,
                    shouldExportAudio,
                    shouldExportTxt,
                    onProgress
                )
            }
            onResult(result)
        }
    }

    private suspend fun pickFolder(): NSURL? = suspendCancellableCoroutine { continuation ->

        val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            ?: run {
                continuation.resume(null)
                return@suspendCancellableCoroutine
            }

        val documentPicker = UIDocumentPickerViewController(
            forOpeningContentTypes = listOf(
                platform.UniformTypeIdentifiers.UTTypeFolder
            )
        )

        documentPicker.allowsMultipleSelection = false
        documentPicker.directoryURL = NSFileManager.defaultManager.URLsForDirectory(
            NSDocumentDirectory,
            NSUserDomainMask
        ).firstOrNull() as? NSURL

        // Create and store delegate with strong reference
        val delegate = DocumentPickerDelegate(
            onPicked = { url ->
                url?.startAccessingSecurityScopedResource()
                delegateRef = null // Clear reference after use
                continuation.resume(url)
            },
            onCancelled = {
                delegateRef = null // Clear reference after use
                continuation.resume(null)
            }
        )

        // Store strong reference to prevent garbage collection
        delegateRef = delegate
        documentPicker.delegate = delegate

        viewController.presentViewController(documentPicker, animated = true, completion = null)
    }

    @OptIn(ExperimentalForeignApi::class)
    private suspend fun performExport(
        folderUrl: NSURL,
        texts: List<String>,
        titles: List<String>,
        audioPath: List<String>,
        shouldExportAudio: Boolean,
        shouldExportTxt: Boolean,
        onProgress: (Float) -> Unit
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val fileManager = NSFileManager.defaultManager
            val timestamp = getFormattedDate(PATTERN_DATE_FORMAT)
            val exportFolderName = "Notes_export_$timestamp"
            val exportFolderUrl = folderUrl.URLByAppendingPathComponent(exportFolderName, isDirectory = true)
                ?: return@withContext Result.failure(Exception("Failed to create folder path"))

            memScoped {
                val error = alloc<ObjCObjectVar<NSError?>>()
                val success = fileManager.createDirectoryAtURL(
                    exportFolderUrl,
                    withIntermediateDirectories = true,
                    attributes = null,
                    error = error.ptr
                )
                if (!success) {
                    return@withContext Result.failure(
                        Exception("Failed to create directory: ${error.value?.localizedDescription}")
                    )
                }
            }

            val textFileNames = mutableListOf<String>()
            val audioFileNames = mutableListOf<String>()

            val totalItems = (if (shouldExportTxt) texts.size else 0) +
                    (if (shouldExportAudio) audioPath.size else 0)
            var completedItems = 0

            if (shouldExportTxt) {
                texts.forEachIndexed { index, text ->
                    val timestampText = getFormattedDate(PATTERN_DATE_FORMAT)
                    val textTitle = (titles[index].takeIf { it.isNotBlank() } ?: TEXT_BLANK_DEFAULT).take(20)
                    val fileName = "${textTitle}-${timestampText}.txt"

                    val fileUrl = exportFolderUrl.URLByAppendingPathComponent(fileName)
                        ?: return@withContext Result.failure(Exception("Failed to create text file path $index"))

                    memScoped {
                        val error = alloc<ObjCObjectVar<NSError?>>()
                        val nsString = text as NSString
                        val success = nsString.writeToURL(
                            fileUrl,
                            atomically = true,
                            encoding = NSUTF8StringEncoding,
                            error = error.ptr
                        )

                        if (!success) {
                            return@withContext Result.failure(
                                Exception("Failed to write text file $index: ${error.value?.localizedDescription}")
                            )
                        }
                    }

                    textFileNames.add(fileName)

                    // Update progress
                    completedItems++
                    val progress = completedItems.toFloat() / totalItems.toFloat()
                    kotlinx.coroutines.MainScope().launch {
                        onProgress(progress)
                    }
                }
            }

            if (shouldExportAudio) {
                audioPath.forEachIndexed { index, path ->
                    if(path.isNotEmpty()) {
                        val timestampAudio = getFormattedDate(PATTERN_DATE_FORMAT)
                        val sourceUrl = NSURL.fileURLWithPath(path)

                        if (!fileManager.fileExistsAtPath(path)) {
                            return@withContext Result.failure(Exception("Audio file not found: $path"))
                        }

                        val textTitle = (titles[index].takeIf { it.isNotBlank() } ?: TEXT_BLANK_DEFAULT).take(20)
                        val audioFileName = "${textTitle}-audio-${timestampAudio}.wav"

                        val destUrl = exportFolderUrl.URLByAppendingPathComponent(audioFileName)
                            ?: return@withContext Result.failure(Exception("Failed to create audio file path $index"))

                        // Copy audio file
                        memScoped {
                            val error = alloc<ObjCObjectVar<NSError?>>()
                            val success = fileManager.copyItemAtURL(
                                sourceUrl,
                                destUrl,
                                error = error.ptr
                            )

                            if (!success) {
                                return@withContext Result.failure(
                                    Exception("Failed to copy audio file $index: ${error.value?.localizedDescription}")
                                )
                            }
                        }

                        audioFileNames.add(audioFileName)

                        completedItems++
                        val progress = completedItems.toFloat() / totalItems.toFloat()
                        kotlinx.coroutines.MainScope().launch {
                            onProgress(progress)
                        }
                    }
                }
            }

            // Create JSON metadata file
            createMetadataJson(exportFolderUrl, textFileNames, audioFileNames)

            // Stop accessing security-scoped resource
            folderUrl.stopAccessingSecurityScopedResource()

            Result.success("Successfully exported ${texts.size} text files and ${audioPath.size} audio files")
        } catch (e: Exception) {
            folderUrl.stopAccessingSecurityScopedResource()
            Result.failure(e)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun createMetadataJson(
        exportFolderUrl: NSURL,
        textFileNames: List<String>,
        audioFileNames: List<String>
    ) {
        try {
            val jsonArray = mutableListOf<Map<String, String>>()
            val maxSize = maxOf(textFileNames.size, audioFileNames.size)

            for (i in 0 until maxSize) {
                val entry = mapOf(
                    "textFile" to (textFileNames.getOrNull(i) ?: ""),
                    "audioFile" to (audioFileNames.getOrNull(i) ?: ""),
                    "timestamp" to getFormattedDate(PATTERN_DATE_FORMAT)
                )
                jsonArray.add(entry)
            }

            memScoped {
                val error = alloc<ObjCObjectVar<NSError?>>()
                val jsonData = NSJSONSerialization.dataWithJSONObject(
                    jsonArray,
                    options = NSJSONWritingPrettyPrinted,
                    error = error.ptr
                ) as? NSData

                if (jsonData == null) {
                    println("Failed to create JSON: ${error.value?.localizedDescription}")
                    return
                }

                val timestampJson = getFormattedDate(PATTERN_DATE_FORMAT)
                val jsonFileName = "metadata-${timestampJson}.json"
                val jsonFileUrl = exportFolderUrl.URLByAppendingPathComponent(jsonFileName)
                    ?: throw Exception("Failed to create JSON file path")

                jsonData.writeToURL(jsonFileUrl, atomically = true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getFormattedDate(pattern: String): String {
        val formatter = NSDateFormatter()
        formatter.dateFormat = pattern
        formatter.locale = NSLocale.currentLocale
        return formatter.stringFromDate(NSDate())
    }
}

// Separate delegate class to maintain strong reference
private class DocumentPickerDelegate(
    private val onPicked: (NSURL?) -> Unit,
    private val onCancelled: () -> Unit
) : NSObject(), UIDocumentPickerDelegateProtocol {

    override fun documentPicker(
        controller: UIDocumentPickerViewController,
        didPickDocumentsAtURLs: List<*>
    ) {
        val url = didPickDocumentsAtURLs.firstOrNull() as? NSURL
        onPicked(url)
    }

    override fun documentPickerWasCancelled(
        controller: UIDocumentPickerViewController
    ) {
        onCancelled()
    }
}
