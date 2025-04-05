package com.module.notelycompose.notes.presentation.list

sealed class NoteListEvent {
    data class OnNoteDeleted(val id: Long) : NoteListEvent()
    data class OnFilterNote(val filter: String) : NoteListEvent()
    data class OnSearchNote(val keyword: String) : NoteListEvent()
}
