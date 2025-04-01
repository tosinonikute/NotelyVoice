package com.module.notelycompose.notes.presentation.mapper

import com.module.notelycompose.notes.domain.model.NoteDomainModel
import com.module.notelycompose.notes.presentation.list.model.NoteUiModel

class NoteUiMapper {
    fun mapToUiModel(domainModel: NoteDomainModel): NoteUiModel {
        return NoteUiModel(
            id = domainModel.id,
            title = domainModel.title,
            content = domainModel.content,
            starred = domainModel.starred,
            createdAt = domainModel.createdAt
        )
    }
}
