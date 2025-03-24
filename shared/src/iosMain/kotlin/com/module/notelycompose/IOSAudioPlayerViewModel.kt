package com.module.notelycompose

import com.module.notelycompose.audio.presentation.AudioPlayerPresentationState
import com.module.notelycompose.audio.presentation.AudioPlayerViewModel
import com.module.notelycompose.audio.presentation.mappers.AudioPlayerPresentationToUiMapper
import com.module.notelycompose.audio.ui.expect.PlatformAudioPlayer
import com.module.notelycompose.audio.ui.player.model.AudioPlayerUiState

class IOSAudioPlayerViewModel(
    private val audioPlayer: PlatformAudioPlayer,
    private val mapper: AudioPlayerPresentationToUiMapper
) {
    private val viewModel by lazy {
        AudioPlayerViewModel(
            audioPlayer = audioPlayer,
            mapper = mapper
        )
    }
    val state = viewModel.uiState

    fun onGetUiState(presentationState: AudioPlayerPresentationState): AudioPlayerUiState {
        return viewModel.onGetUiState(presentationState)
    }

    fun onLoadAudio(filePath: String) {
        viewModel.onLoadAudio(filePath)
    }

    fun onTogglePlayPause() {
        viewModel.onTogglePlayPause()
    }

    fun onSeekTo(position: Int) {
        viewModel.onSeekTo(position)
    }

    fun onCleared() {
        viewModel.onClear()
    }
}
