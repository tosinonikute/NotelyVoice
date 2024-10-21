package com.module.notelycompose.notes.domain

import com.module.notelycompose.core.BabyBlueHex
import com.module.notelycompose.core.LightGreenHex
import com.module.notelycompose.core.RedOrangeHex
import com.module.notelycompose.core.RedPinkHex
import com.module.notelycompose.core.VioletHex
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class Note(
    val id: Int,
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
