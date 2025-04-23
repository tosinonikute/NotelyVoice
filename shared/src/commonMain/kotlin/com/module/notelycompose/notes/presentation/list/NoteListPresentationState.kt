package com.module.notelycompose.notes.presentation.list

import com.module.notelycompose.notes.presentation.list.model.NotePresentationModel

data class NoteListPresentationState(
    val originalNotes: List<NotePresentationModel> = emptyList(),
    val filteredNotes: List<NotePresentationModel> = emptyList(),
    val selectedTabTitle: String
)
