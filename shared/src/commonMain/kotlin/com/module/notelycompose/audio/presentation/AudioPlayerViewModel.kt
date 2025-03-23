package com.module.notelycompose.audio.presentation

import com.module.notelycompose.audio.presentation.mappers.AudioPlayerPresentationToUiMapper
import com.module.notelycompose.audio.ui.expect.PlatformAudioPlayer
import com.module.notelycompose.audio.ui.player.model.AudioPlayerUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Platform-independent ViewModel for audio playback functionality
 */
class AudioPlayerViewModel(
    private val audioPlayer: PlatformAudioPlayer,
    private val mapper: AudioPlayerPresentationToUiMapper,
    coroutineScope: CoroutineScope? = null
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private var progressUpdateJob: Job? = null

    private val _uiState = MutableStateFlow(AudioPlayerPresentationState())
    val uiState: StateFlow<AudioPlayerPresentationState> = _uiState.asStateFlow()

    fun onGetUiState(presentationState: AudioPlayerPresentationState): AudioPlayerUiState {
        return mapper.mapToUiState(presentationState)
    }

    fun onLoadAudio(filePath: String) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val duration = audioPlayer.prepare("/data/user/0/com.module.notelycompose.android/cache/recording_160434.mp3")
                _uiState.update { it.copy(
                    isLoaded = true,
                    duration = duration,
                    isPlaying = false,
                    currentPosition = 0
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    errorMessage = e.message ?: "Failed to load audio"
                ) }
            }
        }
    }

    fun onTogglePlayPause() {
        if (_uiState.value.isPlaying) {
            onPause()
        } else {
            onPlay()
        }
    }

    private fun onPlay() {
        audioPlayer.play()
        _uiState.update { it.copy(isPlaying = true) }
        onStartProgressUpdates()
    }

    private fun onPause() {
        audioPlayer.pause()
        _uiState.update { it.copy(isPlaying = false) }
        onStopProgressUpdates()
    }

    fun onSeekTo(position: Int) {
        audioPlayer.seekTo(position)
        _uiState.update { it.copy(currentPosition = position) }
    }

    private fun onStartProgressUpdates() {
        progressUpdateJob?.cancel()
        progressUpdateJob = viewModelScope.launch {
            while (_uiState.value.isPlaying) {
                val currentPosition = audioPlayer.getCurrentPosition()
                _uiState.update { it.copy(currentPosition = currentPosition) }

                // Check if we've reached the end
                if (currentPosition >= _uiState.value.duration && _uiState.value.duration > 0) {
                    _uiState.update { it.copy(isPlaying = false, currentPosition = 0) }
                    audioPlayer.seekTo(0)
                    onStopProgressUpdates()
                    break
                }

                delay(100)
            }
        }
    }

    private fun onStopProgressUpdates() {
        progressUpdateJob?.cancel()
        progressUpdateJob = null
    }

    /**
     * Call this method when the ViewModel is no longer needed
     * to clean up resources and cancel ongoing jobs
     */
    fun onClear() {
        onStopProgressUpdates()
        audioPlayer.release()
        viewModelScope.cancel()
    }
}

/**
 * Data class representing the UI state of the audio player
 */
data class AudioPlayerPresentationState(
    val isLoaded: Boolean = false,
    val isPlaying: Boolean = false,
    val currentPosition: Int = 0,
    val duration: Int = 0,
    val errorMessage: String? = null
)
