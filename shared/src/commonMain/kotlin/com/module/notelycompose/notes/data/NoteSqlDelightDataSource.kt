package com.module.notelycompose.notes.data

import com.module.notelycompose.core.CommonFlow
import com.module.notelycompose.core.DateTimeUtil
import com.module.notelycompose.core.toCommonFlow
import com.module.notelycompose.database.NoteDatabase
import com.module.notelycompose.notes.domain.Note
import com.module.notelycompose.notes.domain.NoteDataSource
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map

class NoteSqlDelightDataSource(
    db: NoteDatabase
) : NoteDataSource {
    private val queries = db.noteQueries
    override suspend fun insertNote(
        title: String,
        content: String,
    ) {
        queries.insertNote(
            title = title,
            content = content,
            colorHex = Note.generateRandomColor(),
            created_at = DateTimeUtil.toEpochMilli(DateTimeUtil.now())
        )
    }

    override fun getNotes(): CommonFlow<List<Note>> {
        return queries
            .getAllNotes()
            .asFlow()
            .mapToList()
            .map { note ->
                note.map { it.toNote() }
            }.toCommonFlow()
    }

    override fun getNoteById(id: Int): Note? {
        return queries
            .getNoteById(id.toLong())
            .executeAsOneOrNull()?.toNote()
    }

    override suspend fun deleteNoteById(id: Int) {
        queries
            .deleteNoteById(id.toLong())
    }
}