package com.module.notelycompose.notes.presentation.list.mapper

import com.module.notelycompose.notes.domain.model.NotesFilterDomainModel
import com.module.notelycompose.notes.presentation.list.model.NotesFilterPresentationModel

class NotesFilterMapper {
    fun mapStringToPresentationModel(filterStr: String): NotesFilterPresentationModel {
        return when (filterStr.uppercase()) {
            NotesFilterConstants.ALL -> NotesFilterPresentationModel.ALL
            NotesFilterConstants.STARRED -> NotesFilterPresentationModel.STARRED
            NotesFilterConstants.VOICES -> NotesFilterPresentationModel.VOICES
            NotesFilterConstants.RECENT -> NotesFilterPresentationModel.RECENT
            else -> NotesFilterPresentationModel.ALL
        }
    }

    fun mapToDomainModel(presentationFilter: NotesFilterPresentationModel): NotesFilterDomainModel {
        return when (presentationFilter) {
            is NotesFilterPresentationModel.ALL -> NotesFilterDomainModel.ALL
            is NotesFilterPresentationModel.STARRED -> NotesFilterDomainModel.STARRED
            is NotesFilterPresentationModel.VOICES -> NotesFilterDomainModel.VOICES
            is NotesFilterPresentationModel.RECENT -> NotesFilterDomainModel.RECENT
        }
    }
}
