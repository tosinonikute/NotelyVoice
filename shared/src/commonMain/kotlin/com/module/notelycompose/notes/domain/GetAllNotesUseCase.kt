package com.module.notelycompose.notes.domain

import com.module.notelycompose.core.CommonFlow
import com.module.notelycompose.core.toCommonFlow
import com.module.notelycompose.notes.domain.mapper.NoteDomainMapper
import com.module.notelycompose.notes.domain.model.NoteDomainModel
import com.module.notelycompose.notes.domain.model.NotesFilterDomainModel
import com.module.notelycompose.notes.domain.model.NotesFilterDomainModel.ALL
import com.module.notelycompose.notes.domain.model.NotesFilterDomainModel.STARRED
import com.module.notelycompose.notes.domain.model.NotesFilterDomainModel.VOICES
import com.module.notelycompose.notes.domain.model.NotesFilterDomainModel.RECENT
import kotlinx.coroutines.flow.map

class GetAllNotesUseCase(
    private val noteDataSource: NoteDataSource,
    private val noteDomainMapper: NoteDomainMapper
) {
    fun execute(): CommonFlow<List<NoteDomainModel>> {
        return noteDataSource.getNotes()
        .map { notes ->
            notes.map { noteDataModel ->
                noteDomainMapper.mapToDomainModel(noteDataModel)
            }
        }.toCommonFlow()
    }
}
