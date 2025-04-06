package com.module.notelycompose.notes.presentation.detail

import androidx.compose.ui.text.style.TextAlign
import com.module.notelycompose.notes.presentation.detail.model.TextPresentationFormat

sealed class NoteDetailScreenEvent {
    data class DeleteNote(val id: String) : NoteDetailScreenEvent()
    data class NoteSaved(
        val title: String,
        val content: String,
        val formatting: List<TextPresentationFormat>,
        val textAlign: TextAlign
    ) : NoteDetailScreenEvent()
    data class UpdateNote(
        val id: Long,
        val title: String,
        val content: String,
        val formatting: List<TextPresentationFormat>,
        val textAlign: TextAlign
    ) : NoteDetailScreenEvent()
    data class ClearNoteOnEmptyContent(val id: String) : NoteDetailScreenEvent()
}
