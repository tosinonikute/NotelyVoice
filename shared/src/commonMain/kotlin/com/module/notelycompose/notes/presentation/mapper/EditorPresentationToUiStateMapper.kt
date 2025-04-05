package com.module.notelycompose.notes.presentation.mapper

import com.module.notelycompose.notes.presentation.detail.EditorPresentationState
import com.module.notelycompose.notes.presentation.detail.RecordingPathPresentationModel
import com.module.notelycompose.notes.presentation.detail.TextFormatPresentationOption
import com.module.notelycompose.notes.presentation.detail.TextPresentationFormat
import com.module.notelycompose.notes.ui.detail.EditorUiState
import com.module.notelycompose.notes.ui.detail.RecordingPathUiModel
import com.module.notelycompose.notes.ui.detail.TextFormatUiOption
import com.module.notelycompose.notes.ui.detail.TextUiFormat

class EditorPresentationToUiStateMapper {
    fun mapToUiState(presentationState: EditorPresentationState): EditorUiState {
        return EditorUiState(
            content = presentationState.content,
            formats = presentationState.formats.map { mapToTextFormat(it) },
            textAlign = presentationState.textAlign,
            selectionSize = mapToTextFormatUiOption(presentationState.selectionSize),
            recording = mapToRecordingPathUi(presentationState.recording),
            isStarred = presentationState.starred,
            createdAt =  presentationState.createdAt
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

    private fun mapToRecordingPathUi(
        presentation: RecordingPathPresentationModel
    ) = RecordingPathUiModel(
        recordingPath = presentation.recordingPath,
        isRecordingExist = presentation.isRecordingExist
    )
}
