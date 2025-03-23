package com.module.notelycompose.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.notelycompose.audio.presentation.AudioPlayerPresentationState
import com.module.notelycompose.audio.presentation.AudioPlayerViewModel
import com.module.notelycompose.audio.presentation.mappers.AudioPlayerPresentationToUiMapper
import com.module.notelycompose.audio.ui.expect.PlatformAudioPlayer
import com.module.notelycompose.audio.ui.player.model.AudioPlayerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidAudioPlayerViewModel @Inject constructor(
    private val audioPlayer: PlatformAudioPlayer,
    private val mapper: AudioPlayerPresentationToUiMapper
) : ViewModel() {

    private val viewModel by lazy {
        AudioPlayerViewModel(
            audioPlayer = audioPlayer,
            mapper = mapper,
            coroutineScope = viewModelScope
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

    public override fun onCleared() {
        viewModel.onClear()
    }
}
