package com.module.notelycompose.notes.presentation.mapper

import androidx.compose.ui.text.style.TextAlign
import com.module.notelycompose.notes.domain.model.TextAlignDomainModel

class TextAlignPresentationMapper {
    fun mapToDomainModel(textAlign: TextAlign): TextAlignDomainModel {
        return when (textAlign) {
            TextAlign.Left -> TextAlignDomainModel.Left
            TextAlign.Right -> TextAlignDomainModel.Right
            TextAlign.Center -> TextAlignDomainModel.Center
            TextAlign.Justify -> TextAlignDomainModel.Justify
            TextAlign.Start -> TextAlignDomainModel.Start
            TextAlign.End -> TextAlignDomainModel.End
            TextAlign.Unspecified -> TextAlignDomainModel.Start
            else -> TextAlignDomainModel.Start
        }
    }

    fun mapToComposeTextAlign(domainModel: TextAlignDomainModel): TextAlign {
        return when (domainModel) {
            is TextAlignDomainModel.Left -> TextAlign.Left
            is TextAlignDomainModel.Right -> TextAlign.Right
            is TextAlignDomainModel.Center -> TextAlign.Center
            is TextAlignDomainModel.Justify -> TextAlign.Justify
            is TextAlignDomainModel.Start -> TextAlign.Start
            is TextAlignDomainModel.End -> TextAlign.End
        }
    }
}