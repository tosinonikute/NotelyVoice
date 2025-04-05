package com.module.notelycompose.notes.presentation.mapper

import com.module.notelycompose.notes.domain.model.NoteDomainModel
import com.module.notelycompose.notes.presentation.list.model.NoteUiModel
import kotlinx.datetime.LocalDateTime

private const val TIME_STRING = "at"
private const val PAD_START_LENGTH = 2
private const val PAD_CHARACTER = '0'

class NoteUiMapper {
    fun mapToUiModel(domainModel: NoteDomainModel): NoteUiModel {
        return NoteUiModel(
            id = domainModel.id,
            title = domainModel.title,
            content = domainModel.content,
            starred = domainModel.starred,
            createdAt = completeTime(domainModel.createdAt)
        )
    }

    private fun completeTime(createdAt: LocalDateTime): String {
        return "${createdAt.dayOfMonth} ${createdAt.month.toString()
            .lowercase()
            .replaceFirstChar { 
                if (it.isLowerCase()) it.titlecase() else it.toString() }
        } $TIME_STRING ${createdAt.formatTimeWithLeadingZeros()}"
    }

    private fun LocalDateTime.formatTimeWithLeadingZeros(): String {
        val formattedHour = this.hour.toString().padStart(PAD_START_LENGTH, PAD_CHARACTER)
        val formattedMinute = this.minute.toString().padStart(PAD_START_LENGTH, PAD_CHARACTER)
        return "$formattedHour:$formattedMinute"
    }
}
