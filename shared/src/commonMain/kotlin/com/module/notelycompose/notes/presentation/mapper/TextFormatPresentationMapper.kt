package com.module.notelycompose.notes.presentation.mapper

import com.module.notelycompose.notes.domain.model.TextFormatDomainModel
import com.module.notelycompose.notes.presentation.detail.model.TextPresentationFormat

class TextFormatPresentationMapper {
    fun mapToDomainModel(presentationFormat: TextPresentationFormat): TextFormatDomainModel {
        return TextFormatDomainModel(
            range = presentationFormat.range,
            isBold = presentationFormat.isBold,
            isItalic = presentationFormat.isItalic,
            isUnderline = presentationFormat.isUnderline,
            textSize = presentationFormat.textSize
        )
    }

    fun mapToPresentationModel(domainModel: TextFormatDomainModel): TextPresentationFormat {
        return TextPresentationFormat(
            range = domainModel.range,
            isBold = domainModel.isBold,
            isItalic = domainModel.isItalic,
            isUnderline = domainModel.isUnderline,
            textSize = domainModel.textSize
        )
    }
}
