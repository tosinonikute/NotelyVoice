package com.module.notelycompose.notes.domain

import com.module.notelycompose.core.CommonFlow
import com.module.notelycompose.notes.data.model.NoteDataModel
import com.module.notelycompose.notes.data.model.TextAlignDataModel
import com.module.notelycompose.notes.data.model.TextFormatDataModel
import com.module.notelycompose.notes.domain.model.Note


interface NoteDataSource {
    suspend fun insertNote(
        title: String,
        content: String,
        formatting: List<TextFormatDataModel>,
        textAlign: TextAlignDataModel
    )

    suspend fun updateNote(
        id: Long,
        title: String,
        content: String,
        formatting: List<TextFormatDataModel>,
        textAlign: TextAlignDataModel
    )

    fun getNotes(): CommonFlow<List<NoteDataModel>>
    fun getNoteById(id: Long): NoteDataModel?
    fun getLastNote(): NoteDataModel?
    suspend fun deleteNoteById(id: Long)
}
