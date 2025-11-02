package audio.launcher

import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.*
import platform.UIKit.*
import platform.PhotosUI.*
import platform.Photos.*
import platform.darwin.NSObject
import platform.UniformTypeIdentifiers.*

internal class IOSVideoPickerLauncher {

    private var launcherCallback: ((String?) -> Unit)? = null

    @OptIn(ExperimentalForeignApi::class)
    private val pickerDelegate = object : NSObject(), PHPickerViewControllerDelegateProtocol {

        override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
            picker.dismissViewControllerAnimated(true, null)

            val results = didFinishPicking.filterIsInstance<PHPickerResult>()
            if (results.isEmpty()) {
                launcherCallback?.invoke(null)
                return
            }

            val result = results.first()
            val itemProvider = result.itemProvider

            if (itemProvider.hasItemConformingToTypeIdentifier(UTTypeMovie.identifier)) {
                itemProvider.loadFileRepresentationForTypeIdentifier(UTTypeMovie.identifier) { url, error ->
                    if (error != null) {
                        Napier.e("❌ Failed to load video file: ${error.localizedDescription}")
                        launcherCallback?.invoke(null)
                        return@loadFileRepresentationForTypeIdentifier
                    }

                    if (url == null) {
                        Napier.e("❌ No video URL received")
                        launcherCallback?.invoke(null)
                        return@loadFileRepresentationForTypeIdentifier
                    }

                    // Copy video file to app documents directory
                    val fileManager = NSFileManager.defaultManager
                    val documentsDir = fileManager
                        .URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
                        .first() as NSURL

                    val fileName = "imported_video_${NSDate().timeIntervalSince1970.toLong()}.mp4"
                    val outputURL = documentsDir.URLByAppendingPathComponent(fileName)

                    // Remove existing file if any
                    if (fileManager.fileExistsAtPath(outputURL!!.path!!)) {
                        fileManager.removeItemAtURL(outputURL, null)
                    }

                    // Copy the video file
                    val success = fileManager.copyItemAtURL(url, outputURL, null)
                    if (success) {
                        Napier.d("✅ Video copied successfully")
                        launcherCallback?.invoke(outputURL.path)
                    } else {
                        Napier.e("❌ Failed to copy video file")
                        launcherCallback?.invoke(null)
                    }
                }
            } else {
                Napier.e("❌ Item doesn't conform to video type")
                launcherCallback?.invoke(null)
            }
        }
    }

    fun launch(onResult: (String?) -> Unit) {
        launcherCallback = onResult

        var config = PHPickerConfiguration(photoLibrary = PHPhotoLibrary.sharedPhotoLibrary())
        config.selectionLimit = 1
        config.filter = PHPickerFilter.videosFilter()

        val picker = PHPickerViewController(configuration = config)
        picker.delegate = pickerDelegate

        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            viewControllerToPresent = picker,
            animated = true,
            completion = null
        )
    }
}
