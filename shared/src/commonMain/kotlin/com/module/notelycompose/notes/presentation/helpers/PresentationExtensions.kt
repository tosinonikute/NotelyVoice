package com.module.notelycompose.notes.presentation.helpers

import kotlinx.datetime.LocalDateTime

const val DEFAULT_CONTENT = "No additional text"
const val NEW_LINE = "\n"
const val ELLIPSIS = "..."
const val DEFAULT_MAX_LENGTH = 20
const val DATE_STR = "at"
const val MINUTE_PADDING_LENGTH = 2
const val PADDING_CHAR = '0'

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

fun LocalDateTime.formattedDate(): String {
    val day = this.dayOfMonth
    val month = this.month.name.lowercase().replaceFirstChar { it.uppercase() }
    val year = this.year
    val hour = this.hour
    val minute = this.minute.toString().padStart(MINUTE_PADDING_LENGTH, PADDING_CHAR)
    return "$day $month $year $DATE_STR $hour:$minute"
}