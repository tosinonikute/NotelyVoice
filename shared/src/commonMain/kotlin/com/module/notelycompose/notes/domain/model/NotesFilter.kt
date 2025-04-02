package com.module.notelycompose.notes.domain.model

sealed class NotesFilter {
    object ALL : NotesFilter()
    object STARRED : NotesFilter()
    object VOICES : NotesFilter()
}
