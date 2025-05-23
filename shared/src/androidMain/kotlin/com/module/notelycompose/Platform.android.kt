package com.module.notelycompose

class AndroidPlatform(private val version: String) : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
    override val isAndroid: Boolean get() = true
    override val appVersion: String get() = version
}

actual fun getPlatform(): Platform = AndroidPlatform("")
