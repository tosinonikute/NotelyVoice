package com.module.notelycompose.notes.domain

import com.module.notelycompose.notes.domain.mapper.NoteDomainMapper
import com.module.notelycompose.notes.domain.model.NoteDomainModel

class GetLastNote(
    private val noteDataSource: NoteDataSource,
    private val noteDomainMapper: NoteDomainMapper
) {
    fun execute(): NoteDomainModel? {
        return noteDataSource.getLastNote()?.let { noteDomainMapper.mapToDomainModel(it) }
    }
}
