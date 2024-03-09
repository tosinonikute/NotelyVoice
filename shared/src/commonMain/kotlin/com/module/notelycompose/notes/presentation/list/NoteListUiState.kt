package com.module.notelycompose.notes.presentation.list

import com.module.notelycompose.notes.domain.Note

data class NoteListUiState(
    val notes: List<Note> = emptyList(),
)
