package com.module.notelycompose.notes.presentation.list.model

import com.module.notelycompose.core.BabyBlueHex
import com.module.notelycompose.core.LightGreenHex
import com.module.notelycompose.core.RedOrangeHex
import com.module.notelycompose.core.RedPinkHex
import com.module.notelycompose.core.VioletHex
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

// TODo re-modify the Ui model
data class NoteUiModel(
    val id: Long,
    val title: String,
    val content: String,
    val colorHex: Long,
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
) {
    companion object {
        private val colors = listOf(RedOrangeHex, RedPinkHex, LightGreenHex, BabyBlueHex, VioletHex)

        fun generateRandomColor() = colors.random()
    }
}