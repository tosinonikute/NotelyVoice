package com.module.notelycompose.notes.data.model

import com.module.notelycompose.notes.domain.serializer.IntRangeSerializer
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class TextFormatDataModel(
    @Serializable(with = IntRangeSerializer::class)
    val range: IntRange,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isUnderline: Boolean = false,
    val textSize: Float? = null
)
