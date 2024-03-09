package com.module.notelycompose.notes.domain


class DeleteNoteById(
    private val noteDataSource: NoteDataSource
) {
    suspend fun execute(id: Int) {
        return noteDataSource.deleteNoteById(id)
    }
}