package com.module.notelycompose.notes.ui.detail

import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign

private const val TEXT_SIZE_TITLE = 24f
private const val TEXT_SIZE_HEADING = 20f
private const val TEXT_SIZE_SUBHEADING = 16f
private const val TEXT_SIZE_BODY = 14f

data class EditorUiState(
    val content: TextFieldValue = TextFieldValue(""),
    val formats: List<TextUiFormat> = emptyList(),
    val textAlign: TextAlign = TextAlign.Left,
    val selectionSize: TextFormatUiOption = TextUiFormats.Body
)

data class TextUiFormat(
    val range: IntRange,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isUnderline: Boolean = false,
    val textSize: Float? = null
)

data class TextFormatUiOption(
    val size: Float
)

object TextUiFormats {
    val Title = TextFormatUiOption(TEXT_SIZE_TITLE)
    val Heading = TextFormatUiOption(TEXT_SIZE_HEADING)
    val SubHeading = TextFormatUiOption(TEXT_SIZE_SUBHEADING)
    val Body = TextFormatUiOption(TEXT_SIZE_BODY)
    val NoSelection = TextFormatUiOption(0f)
}
