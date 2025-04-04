package com.module.notelycompose.notes.presentation.list

import com.module.notelycompose.notes.presentation.list.model.NoteUiModel

data class NoteListUiState(
    val notes: List<NoteUiModel> = emptyList()
)
