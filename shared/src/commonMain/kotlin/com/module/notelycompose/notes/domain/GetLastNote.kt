package com.module.notelycompose.notes.domain


class GetLastNote(
    private val noteDataSource: NoteDataSource
) {
    fun execute(): Note? {
        return noteDataSource.getLastNote()
    }
}