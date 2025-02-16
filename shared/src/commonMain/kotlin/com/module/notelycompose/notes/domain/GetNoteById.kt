package com.module.notelycompose.notes.domain

import com.module.notelycompose.notes.domain.mapper.NoteDomainMapper
import com.module.notelycompose.notes.domain.model.NoteDomainModel

class GetNoteById(
    private val noteDataSource: NoteDataSource,
    private val noteDomainMapper: NoteDomainMapper
) {
    fun execute(id: Long): NoteDomainModel? {
        return noteDataSource.getNoteById(id)?.let { noteDomainMapper.mapToDomainModel(it) }
    }
}
