package com.module.notelycompose.audio.ui.player.model

data class AudioPlayerUiState(
    val isLoaded: Boolean,
    val isPlaying: Boolean,
    val currentPosition: Int,
    val duration: Int,
    val errorMessage: String
)
