package com.module.notelycompose.notes.domain

class UpdateNoteUseCase(
    private val noteDataSource: NoteDataSource
) {
    suspend fun execute(
        id: Int,
        title: String,
        content: String
    ) {
        noteDataSource.updateNote(
            id = id,
            title = title,
            content = content
        )
    }
}