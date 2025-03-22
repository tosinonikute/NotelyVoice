package com.module.notelycompose.audio.presentation.mappers

import com.module.notelycompose.audio.presentation.AudioRecorderPresentationState
import com.module.notelycompose.audio.ui.recorder.AudioRecorderUiState

class AudioRecorderPresentationToUiStateMapper {
    fun mapToUiState(presentationState: AudioRecorderPresentationState): AudioRecorderUiState {
        return AudioRecorderUiState(
            recordCounterString = presentationState.recordCounterString
        )
    }
}
