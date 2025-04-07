package com.module.notelycompose.notes.presentation.helpers

const val DEFAULT_CONTENT = "No additional text"
const val NEW_LINE = "\n"
const val ELLIPSIS = "..."
const val DEFAULT_MAX_LENGTH = 20

fun String.returnFirstLine(): String {
    return this.split(NEW_LINE).firstOrNull().orEmpty()
}

fun String.truncateWithEllipsis(maxLength: Int = DEFAULT_MAX_LENGTH): String {
    return if (this.length > maxLength) {
        this.take(maxLength) + ELLIPSIS
    } else {
        this
    }
}

fun String.getFirstNonEmptyLineAfterFirst(): String {
    val lines = this.split(NEW_LINE)
    if (lines.size > 1) {
        for (i in 1 until lines.size) {
            if (lines[i].isNotBlank()) {
                return lines[i]
            }
        }
    }
    return DEFAULT_CONTENT
}
