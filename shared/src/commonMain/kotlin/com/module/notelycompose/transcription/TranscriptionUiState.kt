package com.module.notelycompose.transcription

data class TranscriptionUiState(
    val isListening: Boolean = false,
    val viewOriginalText: Boolean = true,
    val finalText: String = "",
    val partialText: String = "",
    val summarizedText: String = "",
    val originalText: String = ""
)