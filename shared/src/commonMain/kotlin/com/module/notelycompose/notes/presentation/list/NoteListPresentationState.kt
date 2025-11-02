package com.module.notelycompose.notes.presentation.list

import com.module.notelycompose.notes.presentation.list.mapper.NotesFilterConstants
import com.module.notelycompose.notes.presentation.list.model.NotePresentationModel

data class NoteListPresentationState(
    val originalNotes: List<NotePresentationModel> = emptyList(),
    val filteredNotes: List<NotePresentationModel> = emptyList(),
    val selectedTabIndex: Int = NotesFilterConstants.ALL,
    val showEmptyContent: Boolean = false,
    val allNotesSizeStr: String = ""
)
