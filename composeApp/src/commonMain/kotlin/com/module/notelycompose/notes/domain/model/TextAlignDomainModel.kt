package com.module.notelycompose.notes.domain.model

sealed class TextAlignDomainModel {
    data object Left : TextAlignDomainModel()
    data object Right : TextAlignDomainModel()
    data object Center : TextAlignDomainModel()
    data object Justify : TextAlignDomainModel()
    data object Start : TextAlignDomainModel()
    data object End : TextAlignDomainModel()
}
