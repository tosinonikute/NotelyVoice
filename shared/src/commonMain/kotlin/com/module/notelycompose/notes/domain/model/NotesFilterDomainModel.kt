package com.module.notelycompose.notes.domain.model

sealed class NotesFilterDomainModel {
    object ALL : NotesFilterDomainModel()
    object STARRED : NotesFilterDomainModel()
    object VOICES : NotesFilterDomainModel()
    object RECENT : NotesFilterDomainModel()
}
