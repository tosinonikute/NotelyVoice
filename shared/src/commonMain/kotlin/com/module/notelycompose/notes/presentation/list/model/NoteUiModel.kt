package com.module.notelycompose.notes.presentation.list.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

// TODO: re-modify UI model
data class NoteUiModel(
    val id: Long,
    val title: String,
    val content: String,
    val starred: Boolean,
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)
