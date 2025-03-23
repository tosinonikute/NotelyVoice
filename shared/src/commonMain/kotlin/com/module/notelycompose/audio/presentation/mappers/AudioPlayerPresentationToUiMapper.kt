package com.module.notelycompose.audio.presentation.mappers

import com.module.notelycompose.audio.presentation.AudioPlayerPresentationState
import com.module.notelycompose.audio.ui.player.model.AudioPlayerUiState

class AudioPlayerPresentationToUiMapper {
    fun mapToUiState(presentationState: AudioPlayerPresentationState): AudioPlayerUiState {
        return AudioPlayerUiState(
            isLoaded = presentationState.isLoaded,
            isPlaying = presentationState.isPlaying,
            currentPosition = presentationState.currentPosition,
            duration = presentationState.duration,
            errorMessage = presentationState.errorMessage.orEmpty()
        )
    }
}
