package com.module.notelycompose.notes.domain.mapper

import com.module.notelycompose.notes.data.model.NoteDataModel
import com.module.notelycompose.notes.data.model.TextAlignDataModel
import com.module.notelycompose.notes.domain.model.NoteDomainModel
import com.module.notelycompose.notes.domain.model.TextAlignDomainModel

class NoteDomainMapper(
    private val textFormatMapper: TextFormatMapper
) {
    fun mapToDomainModel(dataModel: NoteDataModel): NoteDomainModel {
        return NoteDomainModel(
            id = dataModel.id,
            title = dataModel.title,
            content = dataModel.content,
            starred = dataModel.starred,
            formatting = dataModel.formatting.map { textFormatMapper.mapToDomainModel(it) },
            textAlign = mapTextAlignToDomainModel(dataModel.textAlign),
            recordingPath = dataModel.recordingPath,
            createdAt = dataModel.createdAt
        )
    }

    fun mapToDataModel(domainModel: NoteDomainModel): NoteDataModel {
        return NoteDataModel(
            id = domainModel.id,
            title = domainModel.title,
            content = domainModel.content,
            starred = domainModel.starred,
            formatting = domainModel.formatting.map { textFormatMapper.mapToDataModel(it) },
            textAlign = mapTextAlignToDataModel(domainModel.textAlign),
            recordingPath = domainModel.recordingPath,
            createdAt = domainModel.createdAt
        )
    }

    fun mapTextAlignToDomainModel(textAlignDataModel: TextAlignDataModel): TextAlignDomainModel {
        return when (textAlignDataModel) {
            TextAlignDataModel.Left -> TextAlignDomainModel.Left
            TextAlignDataModel.Right -> TextAlignDomainModel.Right
            TextAlignDataModel.Center -> TextAlignDomainModel.Center
            TextAlignDataModel.Justify -> TextAlignDomainModel.Justify
            TextAlignDataModel.Start -> TextAlignDomainModel.Start
            TextAlignDataModel.End -> TextAlignDomainModel.End
        }
    }

    fun mapTextAlignToDataModel(textAlignDomainModel: TextAlignDomainModel): TextAlignDataModel {
        return when (textAlignDomainModel) {
            TextAlignDomainModel.Left -> TextAlignDataModel.Left
            TextAlignDomainModel.Right -> TextAlignDataModel.Right
            TextAlignDomainModel.Center -> TextAlignDataModel.Center
            TextAlignDomainModel.Justify -> TextAlignDataModel.Justify
            TextAlignDomainModel.Start -> TextAlignDataModel.Start
            TextAlignDomainModel.End -> TextAlignDataModel.End
        }
    }
}
