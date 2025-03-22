package com.module.notelycompose.audio.presentation

import com.module.notelycompose.audio.ui.expect.PlatformAudioPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
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
class AudioPlayerViewModel {
    // Create a coroutine scope with SupervisorJob to prevent child failures from canceling the parent
    private val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val audioPlayer = PlatformAudioPlayer()
    private var progressUpdateJob: Job? = null

    private val _uiState = MutableStateFlow(AudioPlayerUiState())
    val uiState: StateFlow<AudioPlayerUiState> = _uiState.asStateFlow()

    fun loadAudio(filePath: String) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val duration = audioPlayer.prepare(filePath)
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

    fun togglePlayPause() {
        if (_uiState.value.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun play() {
        audioPlayer.play()
        _uiState.update { it.copy(isPlaying = true) }
        startProgressUpdates()
    }

    fun pause() {
        audioPlayer.pause()
        _uiState.update { it.copy(isPlaying = false) }
        stopProgressUpdates()
    }

    fun seekTo(position: Int) {
        audioPlayer.seekTo(position)
        _uiState.update { it.copy(currentPosition = position) }
    }

    private fun startProgressUpdates() {
        progressUpdateJob?.cancel()
        progressUpdateJob = viewModelScope.launch {
            while (_uiState.value.isPlaying) {
                val currentPosition = audioPlayer.getCurrentPosition()
                _uiState.update { it.copy(currentPosition = currentPosition) }

                // Check if we've reached the end
                if (currentPosition >= _uiState.value.duration && _uiState.value.duration > 0) {
                    _uiState.update { it.copy(isPlaying = false, currentPosition = 0) }
                    audioPlayer.seekTo(0)
                    stopProgressUpdates()
                    break
                }

                delay(100)
            }
        }
    }

    private fun stopProgressUpdates() {
        progressUpdateJob?.cancel()
        progressUpdateJob = null
    }

    /**
     * Call this method when the ViewModel is no longer needed
     * to clean up resources and cancel ongoing jobs
     */
    fun clear() {
        stopProgressUpdates()
        audioPlayer.release()
        viewModelScope.cancel()
    }
}

/**
 * Data class representing the UI state of the audio player
 */
data class AudioPlayerUiState(
    val isLoaded: Boolean = false,
    val isPlaying: Boolean = false,
    val currentPosition: Int = 0,
    val duration: Int = 0,
    val errorMessage: String? = null
)
