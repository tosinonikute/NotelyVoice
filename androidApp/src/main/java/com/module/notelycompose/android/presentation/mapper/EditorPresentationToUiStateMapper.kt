package com.module.notelycompose.android.presentation.mapper

import com.module.notelycompose.android.presentation.EditorPresentationState
import com.module.notelycompose.android.presentation.TextFormatPresentationOption
import com.module.notelycompose.android.presentation.TextPresentationFormat
import com.module.notelycompose.notes.presentation.detail.userinterface.EditorUiState
import com.module.notelycompose.notes.presentation.detail.userinterface.TextFormatUiOption
import com.module.notelycompose.notes.presentation.detail.userinterface.TextUiFormat
import javax.inject.Inject

class EditorPresentationToUiStateMapper @Inject constructor() {
    fun mapToUiState(presentationState: EditorPresentationState): EditorUiState {
        return EditorUiState(
            content = presentationState.content,
            formats = presentationState.formats.map { mapToTextFormat(it) },
            textAlign = presentationState.textAlign,
            selectionSize = mapToTextFormatUiOption(presentationState.selectionSize)
        )
    }

    private fun mapToTextFormat(presentationFormat: TextPresentationFormat): TextUiFormat {
        return TextUiFormat(
            range = presentationFormat.range,
            isBold = presentationFormat.isBold,
            isItalic = presentationFormat.isItalic,
            isUnderline = presentationFormat.isUnderline,
            textSize = presentationFormat.textSize
        )
    }

    private fun mapToTextFormatUiOption(
        presentationOption: TextFormatPresentationOption
    ): TextFormatUiOption {
        return TextFormatUiOption(
            size = presentationOption.size
        )
    }
}
