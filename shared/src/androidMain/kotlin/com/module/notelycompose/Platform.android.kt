package com.module.notelycompose

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
    override val isAndroid: Boolean get() = true
}

actual fun getPlatform(): Platform = AndroidPlatform()