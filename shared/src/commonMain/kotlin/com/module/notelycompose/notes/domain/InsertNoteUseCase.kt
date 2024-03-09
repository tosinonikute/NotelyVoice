package com.module.notelycompose.notes.domain

class InsertNoteUseCase(
    private val noteDataSource: NoteDataSource
) {
    suspend fun execute(
        title: String,
        content: String
    ) {
        noteDataSource.insertNote(
            title = title,
            content = content
        )
    }
}