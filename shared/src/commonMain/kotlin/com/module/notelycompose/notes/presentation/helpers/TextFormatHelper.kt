package com.module.notelycompose.notes.presentation.helpers

import com.module.notelycompose.notes.presentation.detail.TextPresentationFormat

object TextFormatHelper {
    fun List<TextPresentationFormat>.updateFormats(
        oldText: String,
        newText: String,
        changePosition: Int
    ): List<TextPresentationFormat> {
        val lengthDiff = newText.length - oldText.length
        return this.mapNotNull { format ->
            // Get the actual end position for the format
            val formatEnd = format.range.last.coerceAtMost(newText.length)

            when {
                // If change is before format start, shift the whole range
                changePosition <= format.range.first -> {
                    val newStart = (format.range.first + lengthDiff).coerceAtLeast(0)
                    val newEnd = (format.range.last + lengthDiff).coerceAtLeast(newStart)

                    // Only adjust the format that's immediately after the deletion point
                    val adjustedStart = if (lengthDiff < 0 && changePosition == format.range.first) {
                        newStart + 1
                    } else {
                        newStart
                    }

                    if (adjustedStart < newEnd && adjustedStart < newText.length) {
                        format.copy(range = adjustedStart..newEnd.coerceAtMost(newText.length))
                    } else null
                }
                // If change is within or at the end of format range
                changePosition <= formatEnd -> {
                    // If we're changing text within the format
                    val newEnd = if (changePosition < format.range.last) {
                        // Only adjust the end if we're actually within the format
                        (format.range.last + lengthDiff).coerceAtLeast(changePosition)
                    } else {
                        // If we're at the format end, maintain the original end
                        format.range.last
                    }.coerceAtMost(newText.length)

                    if (format.range.first < newEnd) {
                        format.copy(range = format.range.first..newEnd)
                    } else null
                }
                // If change is after format range, keep format unchanged
                else -> {
                    format
                }
            }
        }
    }

    // Additional utility extension functions
    fun IntRange.contains(index: Int): Boolean = index in first..last
}