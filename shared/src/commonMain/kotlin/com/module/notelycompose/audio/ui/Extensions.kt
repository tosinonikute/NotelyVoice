package com.module.notelycompose.audio.ui

fun Int.formatTimeToMMSS(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return "$minutes:${seconds.toString().padStart(2, '0')}"
}

fun String.keepFirstCharCaseExt(): String =
    if (isEmpty()) "" else first() + substring(1).lowercase()