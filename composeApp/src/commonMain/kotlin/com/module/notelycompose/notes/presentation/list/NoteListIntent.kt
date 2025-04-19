package com.module.notelycompose.notes.presentation.list

import com.module.notelycompose.notes.ui.list.model.NoteUiModel

sealed class NoteListIntent {
    data class OnNoteDeleted(val note: NoteUiModel) : NoteListIntent()
    data class OnFilterNote(val filter: String) : NoteListIntent()
    data class OnSearchNote(val keyword: String) : NoteListIntent()
}
