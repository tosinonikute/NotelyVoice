package com.module.notelycompose.notes.presentation.list

sealed class NoteListIntent {
    data class OnNoteDeleted(val id: Long) : NoteListIntent()
    data class OnFilterNote(val filter: String) : NoteListIntent()
    data class OnSearchNote(val keyword: String) : NoteListIntent()
}
