package com.module.notelycompose.notes.presentation.detail.model

private const val TEXT_SIZE_TITLE = 24f
private const val TEXT_SIZE_HEADING = 20f
private const val TEXT_SIZE_SUBHEADING = 16f
private const val TEXT_SIZE_BODY = 14f
private const val TEXT_NO_SELECTION = 0f

object TextPresentationFormats {
    val Title = TextFormatPresentationOption(TEXT_SIZE_TITLE)
    val Heading = TextFormatPresentationOption(TEXT_SIZE_HEADING)
    val SubHeading = TextFormatPresentationOption(TEXT_SIZE_SUBHEADING)
    val Body = TextFormatPresentationOption(TEXT_SIZE_BODY)
    val NoSelection = TextFormatPresentationOption(TEXT_NO_SELECTION)
}
