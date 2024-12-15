package com.module.notelycompose.notes.presentation.detail

sealed class NoteDetailScreenEvent {
    data class GetNote(val id: String) : NoteDetailScreenEvent()
    data class DeleteNote(val id: String) : NoteDetailScreenEvent()
    data class NoteSaved(val title: String, val content: String) : NoteDetailScreenEvent()
    data class UpdateNote(val id: Int, val title: String, val content: String) : NoteDetailScreenEvent()
}
