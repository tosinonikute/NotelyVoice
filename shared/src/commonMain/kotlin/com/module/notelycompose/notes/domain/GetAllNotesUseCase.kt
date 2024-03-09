package com.module.notelycompose.notes.domain

import com.module.notelycompose.core.CommonFlow

class GetAllNotesUseCase(
    private val noteDataSource: NoteDataSource
) {
    fun execute(): CommonFlow<List<Note>> {
        return noteDataSource.getNotes()
    }
}