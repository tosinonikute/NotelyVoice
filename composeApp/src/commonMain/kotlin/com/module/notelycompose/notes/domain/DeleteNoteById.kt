package com.module.notelycompose.notes.domain


class DeleteNoteById(
    private val noteDataSource: NoteDataSource
) {
    suspend fun execute(id: Long) {
        return noteDataSource.deleteNoteById(id)
    }
}