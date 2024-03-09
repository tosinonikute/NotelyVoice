package com.module.notelycompose.notes.data

import com.module.notelycompose.notes.domain.Note
import database.NotesEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun NotesEntity.toNote(): Note {
    return Note(
        id = id.toInt(),
        title = title,
        content = content,
        colorHex = colorHex,
        createdAt = Instant.fromEpochMilliseconds(created_at)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    )
}