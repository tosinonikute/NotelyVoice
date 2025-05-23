package com.module.notelycompose

import platform.UIKit.UIDevice
import platform.Foundation.NSBundle

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val isAndroid: Boolean = UIDevice.currentDevice.systemName.lowercase().contains("ios").not()
    override val appVersion: String get() {
        return NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "Unknown"
    }
}

actual fun getPlatform(): Platform = IOSPlatform()