package com.module.notelycompose.notes.domain.model

data class TextFormatDomainModel(
    val range: IntRange,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isUnderline: Boolean = false,
    val textSize: Float? = null
)
