package com.module.notelycompose.notes.presentation.mapper

import com.module.notelycompose.notes.domain.model.NoteDomainModel
import com.module.notelycompose.notes.presentation.list.model.NotePresentationModel
import com.module.notelycompose.notes.ui.list.model.NoteUiModel
import kotlinx.datetime.LocalDateTime

private const val TIME_STRING = "at"
private const val PAD_START_LENGTH = 2
private const val PAD_CHARACTER = '0'

class NotePresentationMapper {
    fun mapToPresentationModel(domainModel: NoteDomainModel): NotePresentationModel {
        return NotePresentationModel(
            id = domainModel.id,
            title = domainModel.title,
            content = domainModel.content,
            isStarred = domainModel.starred,
            isVoice = domainModel.recordingPath.isNotEmpty(),
            createdAt = completeTime(domainModel.createdAt),
            recordingPath = domainModel.recordingPath,
            words = countWords(domainModel.content)
        )
    }

    private fun completeTime(createdAt: LocalDateTime): String {
        return "${createdAt.dayOfMonth} ${createdAt.month.toString()
            .lowercase()
            .replaceFirstChar { 
                if (it.isLowerCase()) it.titlecase() else it.toString() }
        } $TIME_STRING ${formatTimeWithLeadingZeros(createdAt)}"
    }

    private fun formatTimeWithLeadingZeros(localDateTime: LocalDateTime): String {
        val formattedHour = localDateTime.hour.toString().padStart(PAD_START_LENGTH, PAD_CHARACTER)
        val formattedMinute = localDateTime.minute.toString().padStart(PAD_START_LENGTH, PAD_CHARACTER)
        return "$formattedHour:$formattedMinute"
    }

    private fun countWords(str: String): Int {
        if (str.isBlank()) {
            return 0
        }
        return str.trim().split("\\s+".toRegex()).size
    }

    fun mapToUiModel(presentationModel: NotePresentationModel): NoteUiModel {
        return NoteUiModel(
            id = presentationModel.id,
            title = presentationModel.title,
            content = presentationModel.content,
            isStarred = presentationModel.isStarred,
            isVoice = presentationModel.isVoice,
            createdAt = presentationModel.createdAt,
            recordingPath = presentationModel.recordingPath,
            words = presentationModel.words
        )
    }
}
