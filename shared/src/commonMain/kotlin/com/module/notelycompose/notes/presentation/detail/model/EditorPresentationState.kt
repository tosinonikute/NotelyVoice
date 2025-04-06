package com.module.notelycompose.notes.presentation.detail.model

import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign

data class EditorPresentationState(
    val content: TextFieldValue = TextFieldValue(""),
    val formats: List<TextPresentationFormat> = emptyList(),
    val textAlign: TextAlign = TextAlign.Left,
    val selectionSize: TextFormatPresentationOption = TextPresentationFormats.NoSelection,
    val recording: RecordingPathPresentationModel = RecordingPathPresentationModel(),
    val starred: Boolean = false,
    val createdAt: String = ""
)
