package com.module.notelycompose

import com.module.notelycompose.core.DatabaseDriverFactory
import com.module.notelycompose.database.NoteDatabase
import com.module.notelycompose.notes.data.NoteSqlDelightDataSource
import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetAllNotesUseCase
import com.module.notelycompose.notes.domain.GetNoteById
import com.module.notelycompose.notes.domain.InsertNoteUseCase
import com.module.notelycompose.notes.domain.NoteDataSource

class AppModule {
    val noteDataSource: NoteDataSource by lazy {
        NoteSqlDelightDataSource(NoteDatabase(DatabaseDriverFactory().create()))
    }

    val getAllNotesUseCase: GetAllNotesUseCase by lazy {
        GetAllNotesUseCase(noteDataSource)
    }

    val deleteNoteById: DeleteNoteById by lazy {
        DeleteNoteById(noteDataSource)
    }

    val getNoteById: GetNoteById by lazy {
        GetNoteById(noteDataSource)
    }

    val insertNote: InsertNoteUseCase by lazy {
        InsertNoteUseCase(noteDataSource)
    }
}