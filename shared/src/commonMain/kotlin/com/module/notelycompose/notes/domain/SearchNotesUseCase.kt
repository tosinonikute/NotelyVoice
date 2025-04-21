package com.module.notelycompose.notes.domain

import com.module.notelycompose.core.CommonFlow
import com.module.notelycompose.core.toCommonFlow
import com.module.notelycompose.notes.domain.mapper.NoteDomainMapper
import com.module.notelycompose.notes.domain.model.NoteDomainModel
import kotlinx.coroutines.flow.map

class SearchNotesUseCase(
    private val noteDataSource: NoteDataSource,
    private val noteDomainMapper: NoteDomainMapper
) {
    fun execute(keyword: String): CommonFlow<List<NoteDomainModel>> {
        return noteDataSource.getNotesByKeyword(keyword).map { notes ->
            notes.map { noteDataModel ->
                noteDomainMapper.mapToDomainModel(noteDataModel)
            }
        }.toCommonFlow()
    }
}
