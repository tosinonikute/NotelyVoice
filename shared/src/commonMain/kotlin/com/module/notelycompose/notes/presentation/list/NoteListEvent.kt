package com.module.notelycompose.notes.presentation.list

sealed class NoteListEvent {
    data class OnNoteDeleted(val id: Long) : NoteListEvent()

    data class InsertNote(val title: String, val content: String) : NoteListEvent()
}
