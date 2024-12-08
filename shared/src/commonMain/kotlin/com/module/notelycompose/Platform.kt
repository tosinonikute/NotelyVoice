package com.module.notelycompose

interface Platform {
    val name: String
    val isAndroid: Boolean
}

expect fun getPlatform(): Platform