package com.module.notelycompose.notes.presentation.helpers

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.module.notelycompose.notes.presentation.detail.model.EditorPresentationState
import com.module.notelycompose.notes.presentation.detail.model.TextFormatPresentationOption
import com.module.notelycompose.notes.presentation.detail.model.TextPresentationFormat
import com.module.notelycompose.notes.presentation.detail.model.TextPresentationFormats
import com.module.notelycompose.notes.presentation.helpers.TextFormatHelper.updateFormats

class TextEditorHelper {

    fun updateContent(
        newContent: TextFieldValue,
        currentState: EditorPresentationState,
        getFormattedDate: () -> String,
        updateState: (EditorPresentationState) -> Unit
    ) {
        try {
            val oldText = currentState.content.text
            val newText = newContent.text
            val selection = newContent.selection

            val updatedFormats = currentState.formats
                .updateFormats(oldText, newText, selection.start)

            // Handle Enter key press and bullet points
            if (newText.length > oldText.length && selection.start > 0 &&
                selection.start <= newText.length &&
                newText[selection.start - 1] == '\n'
            ) {
                val bulletResult = handleBulletListContinuation(newText, selection, updatedFormats, currentState)
                if (bulletResult != null) {
                    updateState(
                        bulletResult.copy(
                            selectionSize = getSizeLabel(newContent, updatedFormats)
                        )
                    )
                    return
                }
            }

            updateState(
                currentState.copy(
                    content = newContent,
                    formats = updatedFormats,
                    selectionSize = getSizeLabel(newContent, updatedFormats),
                    createdAt = getFormattedDate()
                )
            )
        } catch (e: Exception) {
            updateState(
                currentState.copy(
                    content = newContent,
                    selectionSize = getSizeLabel(newContent, currentState.formats),
                    createdAt = getFormattedDate()
                )
            )
        }
    }

    private fun handleBulletListContinuation(
        newText: String,
        selection: TextRange,
        updatedFormats: List<TextPresentationFormat>,
        currentState: EditorPresentationState
    ): EditorPresentationState? {
        val previousLineEnd = (selection.start - 1).coerceIn(0, newText.length)
        val textBeforeCursor = newText.substring(0, previousLineEnd)
        val lastNewLineIndex = textBeforeCursor.lastIndexOf('\n')
        val previousLineStart = if (lastNewLineIndex == -1) 0 else (lastNewLineIndex + 1)
        val previousLine = textBeforeCursor.substring(previousLineStart, previousLineEnd)

        // Check if the previous line was an empty bullet point
        if (previousLine.trim() == "•" || previousLine.trim() == "• ") {
            // Remove the empty bullet point and add a new line
            val textWithoutEmptyBullet = newText.substring(0, previousLineStart) +
                    "\n" +
                    newText.substring(selection.start)

            return currentState.copy(
                content = TextFieldValue(
                    text = textWithoutEmptyBullet,
                    selection = TextRange(previousLineStart + 1)
                ),
                formats = updatedFormats
            )
        }

        // Handle normal bullet point continuation
        if (previousLine.trimStart().startsWith("• ")) {
            val indentation = previousLine.takeWhile { it.isWhitespace() }
            val beforeCursor = newText.substring(0, selection.start)
            val afterCursor = if (selection.start < newText.length) {
                newText.substring(selection.start)
            } else ""

            val textWithNewBullet = beforeCursor + indentation + "• " + afterCursor
            val newCursorPosition = (selection.start + indentation.length + 2)
                .coerceIn(0, textWithNewBullet.length)

            return currentState.copy(
                content = TextFieldValue(
                    text = textWithNewBullet,
                    selection = TextRange(newCursorPosition)
                ),
                formats = updatedFormats
            )
        }

        return null
    }

    fun toggleFormat(
        currentState: EditorPresentationState,
        transform: (TextPresentationFormat) -> TextPresentationFormat,
        updateState: (EditorPresentationState) -> Unit
    ) {
        val selection = currentState.content.selection
        if (selection.start == selection.end) return

        val start = selection.start.coerceIn(0, currentState.content.text.length)
        val end = selection.end.coerceIn(0, currentState.content.text.length)

        val existingFormat = currentState.formats.find {
            it.range.contains(start) && it.range.contains(end - 1)
        }

        val newFormat = transform(existingFormat ?: TextPresentationFormat(start..end))

        updateState(
            currentState.copy(
                formats = currentState.formats.filter {
                    !it.range.overlaps(start..end)
                } + newFormat
            )
        )
    }

    fun getSizeLabel(
        content: TextFieldValue,
        formats: List<TextPresentationFormat>
    ): TextFormatPresentationOption {
        return if (content.selection.start == content.selection.end) {
            TextPresentationFormats.NoSelection
        } else {
            formats.find { it.range.contains(content.selection.start) }
                ?.textSize?.let { size ->
                    when (size) {
                        24f -> TextPresentationFormats.Title
                        20f -> TextPresentationFormats.Heading
                        16f -> TextPresentationFormats.SubHeading
                        else -> TextPresentationFormats.Body
                    }
                } ?: TextPresentationFormats.Body
        }
    }

    fun toggleBulletList(
        currentState: EditorPresentationState,
        updateState: (EditorPresentationState) -> Unit
    ) {
        val selection = currentState.content.selection
        val text = currentState.content.text

        try {
            // Handle case when no text is selected
            if (selection.start == selection.end) {
                // Get the current line
                val lineStart = text.lastIndexOf('\n', selection.start - 1).let {
                    if (it == -1) 0 else it + 1
                }
                val lineEnd = text.indexOf('\n', selection.start).let {
                    if (it == -1) text.length else it
                }
                val currentLine = text.substring(lineStart, lineEnd)

                // Create new text with bullet point
                val newText = buildString {
                    append(text.substring(0, lineStart))
                    // Only add bullet if line doesn't already have one
                    if (!currentLine.trimStart().startsWith("• ")) {
                        append("• ")
                        append(currentLine)
                    } else {
                        append(currentLine.replaceFirst("• ", ""))
                    }
                    if (lineEnd < text.length) {
                        append(text.substring(lineEnd))
                    }
                }

                val newCursorPosition = if (!currentLine.trimStart().startsWith("• ")) {
                    lineStart + 2 + currentLine.length
                } else {
                    lineStart + currentLine.length - 2
                }

                updateState(
                    currentState.copy(
                        content = TextFieldValue(
                            text = newText,
                            selection = TextRange(newCursorPosition)
                        )
                    )
                )
                return
            }

            // Original logic for selected text
            val selectedText = text.substring(selection.start, selection.end)
            val lines = selectedText.split("\n")

            val processedLines = lines.map { line ->
                if (line.trim().startsWith("• ")) {
                    line.replaceFirst("• ", "")
                } else if (line.isNotEmpty()) {
                    "• $line"
                } else {
                    line
                }
            }

            val newText = buildString {
                append(text.substring(0, selection.start))
                append(processedLines.joinToString("\n"))
                if (selection.end < text.length) {
                    append(text.substring(selection.end))
                }
            }

            updateState(
                currentState.copy(
                    content = TextFieldValue(
                        text = newText,
                        selection = TextRange(
                            selection.start,
                            (selection.start + processedLines.joinToString("\n").length)
                                .coerceIn(0, newText.length)
                        )
                    )
                )
            )
        } catch (e: Exception) {
            // If any error occurs, keep the current state
            return
        }
    }

    fun refreshSelection(
        currentState: EditorPresentationState,
        updateState: (EditorPresentationState) -> Unit
    ) {
        updateState(
            currentState.copy(
                content = currentState.content.copy(
                    selection = TextRange(
                        currentState.content.selection.start,
                        currentState.content.selection.end
                    )
                )
            )
        )
    }

    // Extension function for IntRange
    private fun IntRange.overlaps(other: IntRange): Boolean =
        first <= other.last && other.first <= last
}