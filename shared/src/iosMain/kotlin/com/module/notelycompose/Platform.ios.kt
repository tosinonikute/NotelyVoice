package com.module.notelycompose

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val isAndroid: Boolean = UIDevice.currentDevice.systemName.lowercase().contains("ios").not()
}

actual fun getPlatform(): Platform = IOSPlatform()