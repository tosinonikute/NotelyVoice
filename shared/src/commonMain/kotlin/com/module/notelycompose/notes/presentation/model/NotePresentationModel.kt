package com.module.notelycompose.notes.presentation.model

import com.module.notelycompose.notes.domain.model.TextAlignDomainModel
import com.module.notelycompose.notes.domain.model.TextFormatDomainModel
import kotlinx.datetime.LocalDateTime

data class NotePresentationModel(
    val id: Long,
    val title: String,
    val content: String,
    val colorHex: Long,
    val formatting: List<TextFormatDomainModel>,
    val textAlign: TextAlignDomainModel,
    val createdAt: LocalDateTime
)
