package com.module.notelycompose.transcription

data class TranscriptionUiState(
    val isLoading: Boolean = false,
    val recordingPath: String = "",
    val text: String = "",
    val summarizedText: String = ""
)