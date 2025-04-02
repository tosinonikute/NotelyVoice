package com.module.notelycompose.notes.domain

import com.module.notelycompose.core.CommonFlow
import com.module.notelycompose.core.toCommonFlow
import com.module.notelycompose.notes.domain.mapper.NoteDomainMapper
import com.module.notelycompose.notes.domain.model.NoteDomainModel
import com.module.notelycompose.notes.domain.model.NotesFilter
import com.module.notelycompose.notes.domain.model.NotesFilter.ALL
import com.module.notelycompose.notes.domain.model.NotesFilter.STARRED
import com.module.notelycompose.notes.domain.model.NotesFilter.VOICES
import kotlinx.coroutines.flow.map

class GetAllNotesUseCase(
    private val noteDataSource: NoteDataSource,
    private val noteDomainMapper: NoteDomainMapper
) {
    fun execute(filter: NotesFilter = ALL): CommonFlow<List<NoteDomainModel>> {
        return when (filter) {
            is ALL -> noteDataSource.getNotes()
            is STARRED -> noteDataSource.getStarredNotes()
            is VOICES -> noteDataSource.getVoiceNotes()
        }.map { notes ->
            notes.map { noteDataModel ->
                noteDomainMapper.mapToDomainModel(noteDataModel)
            }
        }.toCommonFlow()
    }
}
