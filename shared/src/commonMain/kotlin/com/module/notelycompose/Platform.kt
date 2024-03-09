package com.module.notelycompose

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform