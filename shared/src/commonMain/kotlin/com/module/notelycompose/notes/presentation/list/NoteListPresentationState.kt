package com.module.notelycompose.notes.presentation.list

import com.module.notelycompose.notes.presentation.list.model.NotePresentationModel

data class NoteListPresentationState(
    val notes: List<NotePresentationModel> = emptyList(),
    val selectedTabTitle: String = ""
)
