package com.module.notelycompose.notes.presentation.detail.userinterface

fun textSizeSelectedFormats(
    formatOption: FormatOptionTextFormat,
    onSelectFormatOption: (size: Float) -> Unit,
) {
    when (formatOption) {
        FormatOptionTextFormat.Title -> onSelectFormatOption(24f)
        FormatOptionTextFormat.Heading -> onSelectFormatOption(20f)
        FormatOptionTextFormat.Subheading -> onSelectFormatOption(16f)
        FormatOptionTextFormat.Body -> onSelectFormatOption(14f)
    }
}
