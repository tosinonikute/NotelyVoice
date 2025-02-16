package com.module.notelycompose.notes.data.model

import com.module.notelycompose.core.BabyBlueHex
import com.module.notelycompose.core.LightGreenHex
import com.module.notelycompose.core.RedOrangeHex
import com.module.notelycompose.core.RedPinkHex
import com.module.notelycompose.core.VioletHex
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class NoteDataModel(
    val id: Long,
    val title: String,
    val content: String,
    val colorHex: Long,
    val formatting: List<TextFormatDataModel>,
    val textAlign: TextAlignDataModel,
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
) {
    companion object {
        private val colors = listOf(RedOrangeHex, RedPinkHex, LightGreenHex, BabyBlueHex, VioletHex)

        fun generateRandomColor() = colors.random()
    }
}
