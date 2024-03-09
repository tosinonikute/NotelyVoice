package com.module.notelycompose.notes.domain


class GetNoteById(
    private val noteDataSource: NoteDataSource
) {
    fun execute(id: String): Note? {
        return noteDataSource.getNoteById(id.toInt())
    }
}