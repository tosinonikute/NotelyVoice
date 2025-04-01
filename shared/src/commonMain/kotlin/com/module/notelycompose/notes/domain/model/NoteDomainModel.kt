package com.module.notelycompose.notes.domain.model

import kotlinx.datetime.LocalDateTime

data class NoteDomainModel(
    val id: Long,
    val title: String,
    val content: String,
    val starred: Boolean,
    val formatting: List<TextFormatDomainModel>,
    val textAlign: TextAlignDomainModel,
    val recordingPath: String,
    val createdAt: LocalDateTime
)
