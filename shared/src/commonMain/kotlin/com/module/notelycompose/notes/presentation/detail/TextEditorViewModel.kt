package com.module.notelycompose.notes.presentation.detail

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import com.module.notelycompose.notes.presentation.detail.userinterface.EditorUiState
import com.module.notelycompose.notes.presentation.mapper.EditorPresentationToUiStateMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

private const val TEXT_SIZE_TITLE = 24f
private const val TEXT_SIZE_HEADING = 20f
private const val TEXT_SIZE_SUBHEADING = 16f
private const val TEXT_SIZE_BODY = 14f

data class TextPresentationFormat(
    val range: IntRange,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isUnderline: Boolean = false,
    val textSize: Float? = null
)

data class EditorPresentationState(
    val content: TextFieldValue = TextFieldValue(""),
    val formats: List<TextPresentationFormat> = emptyList(),
    val textAlign: TextAlign = TextAlign.Left,
    val selectionSize: TextFormatPresentationOption = TextPresentationFormats.NoSelection
)

data class TextFormatPresentationOption(
    val size: Float
)

object TextPresentationFormats {
    val Title = TextFormatPresentationOption(TEXT_SIZE_TITLE)
    val Heading = TextFormatPresentationOption(TEXT_SIZE_HEADING)
    val SubHeading = TextFormatPresentationOption(TEXT_SIZE_SUBHEADING)
    val Body = TextFormatPresentationOption(TEXT_SIZE_BODY)
    val NoSelection = TextFormatPresentationOption(0f)
}

class TextEditorViewModel (
    private val mapper: EditorPresentationToUiStateMapper
) {

    private val _editorPresentationState = MutableStateFlow(EditorPresentationState())
    val editorPresentationState: StateFlow<EditorPresentationState> = _editorPresentationState

    fun onGetUiState(presentationState: EditorPresentationState): EditorUiState {
        return mapper.mapToUiState(presentationState)
    }

    fun onUpdateContent(newContent: TextFieldValue) {
        try {
            val oldText = _editorPresentationState.value.content.text
            val newText = newContent.text
            val selection = newContent.selection

            // Update formats when text changes
            val updatedFormats = updateFormats(
                _editorPresentationState.value.formats,
                oldText,
                newText,
                selection.start
            )

            // Handle Enter key press and bullet points
            if (newText.length > oldText.length && selection.start > 0 &&
                selection.start <= newText.length &&
                newText[selection.start - 1] == '\n'
            ) {
                val bulletResult = handleBulletListContinuation(newText, selection, updatedFormats)
                if (bulletResult != null) {
                    _editorPresentationState.update {
                        bulletResult.copy(
                            selectionSize = getSizeLabel(newContent, updatedFormats)
                        )
                    }
                    return
                }
            }

            _editorPresentationState.update { currentState ->
                currentState.copy(
                    content = newContent,
                    formats = updatedFormats,
                    selectionSize = getSizeLabel(newContent, updatedFormats)
                )
            }
        } catch (e: Exception) {
            _editorPresentationState.update {
                it.copy(
                    content = newContent,
                    selectionSize = getSizeLabel(newContent, it.formats)
                )
            }
        }
    }

    private fun handleBulletListContinuation(
        newText: String,
        selection: TextRange,
        updatedFormats: List<TextPresentationFormat>
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

            return _editorPresentationState.value.copy(
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

            return _editorPresentationState.value.copy(
                content = TextFieldValue(
                    text = textWithNewBullet,
                    selection = TextRange(newCursorPosition)
                ),
                formats = updatedFormats
            )
        }

        return null
    }

    private fun updateFormats(
        formats: List<TextPresentationFormat>,
        oldText: String,
        newText: String,
        changePosition: Int
    ): List<TextPresentationFormat> {
        val lengthDiff = newText.length - oldText.length
        return formats.mapNotNull { format ->
            when {
                changePosition <= format.range.first -> {
                    val newStart = (format.range.first + lengthDiff).coerceAtLeast(0)
                    val newEnd = (format.range.last + lengthDiff).coerceAtLeast(newStart)
                    if (newStart < newEnd) {
                        format.copy(range = newStart..newEnd)
                    } else null
                }
                changePosition < format.range.last -> {
                    val newEnd = (format.range.last + lengthDiff).coerceAtLeast(format.range.first)
                    format.copy(range = format.range.first..newEnd)
                }
                else -> format
            }
        }
    }

    fun onToggleBold() {
        toggleFormat { it.copy(isBold = !it.isBold) }
        refreshSelection()
    }

    fun onToggleItalic() {
        toggleFormat { it.copy(isItalic = !it.isItalic) }
        refreshSelection()
    }

    fun setTextSize(size: Float) {
        toggleFormat { it.copy(textSize = size) }
        refreshSelection()
    }

    fun onToggleUnderline() {
        toggleFormat { it.copy(isUnderline = !it.isUnderline) }
        refreshSelection()
    }

    private fun refreshSelection() {
        _editorPresentationState.update { current ->
            current.copy(
                content = current.content.copy(
                    selection = TextRange(
                        current.content.selection.start,
                        current.content.selection.end
                    )
                )
            )
        }
    }

    private fun getSizeLabel(
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

    private fun toggleFormat(transform: (TextPresentationFormat) -> TextPresentationFormat) {
        val selection = _editorPresentationState.value.content.selection
        if (selection.start == selection.end) return

        val start = selection.start.coerceIn(0, _editorPresentationState.value.content.text.length)
        val end = selection.end.coerceIn(0, _editorPresentationState.value.content.text.length)

        _editorPresentationState.update { currentState ->
            val existingFormat = currentState.formats.find {
                it.range.contains(start) && it.range.contains(end - 1)
            }

            val newFormat = transform(existingFormat ?: TextPresentationFormat(start..end))

            currentState.copy(
                formats = currentState.formats.filter {
                    !it.range.overlaps(start..end)
                } + newFormat
            )
        }
    }

    fun onSetAlignment(alignment: TextAlign) {
        _editorPresentationState.update { it.copy(textAlign = alignment) }
    }

    fun onToggleBulletList() {
        val currentState = _editorPresentationState.value
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

                _editorPresentationState.update { currentState ->
                    currentState.copy(
                        content = TextFieldValue(
                            text = newText,
                            selection = TextRange(newCursorPosition)
                        )
                    )
                }
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

            _editorPresentationState.update { currentState ->
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
            }
        } catch (e: Exception) {
            // If any error occurs, keep the current state
            return
        }
    }

    private fun IntRange.contains(index: Int): Boolean =
        index in first..last

    private fun IntRange.overlaps(other: IntRange): Boolean =
        first <= other.last && other.first <= last
}