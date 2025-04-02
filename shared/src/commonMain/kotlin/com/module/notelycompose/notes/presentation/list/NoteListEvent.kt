package com.module.notelycompose.notes.presentation.list

sealed class NoteListEvent {
    data class OnNoteDeleted(val id: Long) : NoteListEvent()
    object OnStarredNote : NoteListEvent()
    object OnVoiceNote : NoteListEvent()
}
