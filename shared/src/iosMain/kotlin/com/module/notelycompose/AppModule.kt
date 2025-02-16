package com.module.notelycompose

import com.module.notelycompose.core.DatabaseDriverFactory
import com.module.notelycompose.database.NoteDatabase
import com.module.notelycompose.notes.data.NoteSqlDelightDataSource
import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetAllNotesUseCase
import com.module.notelycompose.notes.domain.GetLastNote
import com.module.notelycompose.notes.domain.GetNoteById
import com.module.notelycompose.notes.domain.InsertNoteUseCase
import com.module.notelycompose.notes.domain.NoteDataSource
import com.module.notelycompose.notes.domain.UpdateNoteUseCase
import com.module.notelycompose.notes.domain.mapper.NoteDomainMapper
import com.module.notelycompose.notes.domain.mapper.TextFormatMapper
import com.module.notelycompose.notes.presentation.mapper.EditorPresentationToUiStateMapper
import com.module.notelycompose.notes.presentation.mapper.NoteUiMapper
import com.module.notelycompose.notes.presentation.mapper.TextAlignPresentationMapper
import com.module.notelycompose.notes.presentation.mapper.TextFormatPresentationMapper

class AppModule {
    val noteDataSource: NoteDataSource by lazy {
        NoteSqlDelightDataSource(NoteDatabase(DatabaseDriverFactory().create()))
    }

    val getAllNotesUseCase: GetAllNotesUseCase by lazy {
        GetAllNotesUseCase(noteDataSource, noteDomainMapper)
    }

    val deleteNoteById: DeleteNoteById by lazy {
        DeleteNoteById(noteDataSource)
    }

    val getNoteById: GetNoteById by lazy {
        GetNoteById(noteDataSource, noteDomainMapper)
    }

    val insertNote: InsertNoteUseCase by lazy {
        InsertNoteUseCase(noteDataSource, textFormatMapper, noteDomainMapper)
    }

    val updateNote: UpdateNoteUseCase by lazy {
        UpdateNoteUseCase(noteDataSource, textFormatMapper, noteDomainMapper)
    }

    val getLastNoteUseCase: GetLastNote by lazy {
        GetLastNote(noteDataSource, noteDomainMapper)
    }

    val noteDomainMapper: NoteDomainMapper by lazy {
        NoteDomainMapper(textFormatMapper)
    }

    val textFormatMapper: TextFormatMapper by lazy { TextFormatMapper() }

    val editorPresentationToUiStateMapper: EditorPresentationToUiStateMapper by lazy {
        EditorPresentationToUiStateMapper()
    }

    val textFormatPresentationMapper: TextFormatPresentationMapper by lazy {
        TextFormatPresentationMapper()
    }

    val textAlignPresentationMapper: TextAlignPresentationMapper by lazy {
        TextAlignPresentationMapper()
    }

    val noteUiMapper: NoteUiMapper by lazy { NoteUiMapper() }
}