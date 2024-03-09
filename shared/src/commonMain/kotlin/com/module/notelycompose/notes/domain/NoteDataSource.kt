package com.module.notelycompose.notes.domain

import com.module.notelycompose.core.CommonFlow


interface NoteDataSource {
    suspend fun insertNote(
        title: String,
        content: String
    )

    fun getNotes(): CommonFlow<List<Note>>
    fun getNoteById(id: Int): Note?
    suspend fun deleteNoteById(id: Int)
}