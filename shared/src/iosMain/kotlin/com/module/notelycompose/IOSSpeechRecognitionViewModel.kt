package com.module.notelycompose

import com.module.notelycompose.audio.presentation.AudioRecorderPresentationState
import com.module.notelycompose.audio.presentation.AudioRecorderViewModel
import com.module.notelycompose.audio.presentation.SpeechRecognitionViewModel
import com.module.notelycompose.audio.presentation.mappers.AudioRecorderPresentationToUiMapper
import com.module.notelycompose.audio.ui.expect.AudioRecorder
import com.module.notelycompose.audio.ui.expect.SpeechRecognizer
import com.module.notelycompose.audio.ui.recorder.AudioRecorderUiState

class IOSSpeechRecognitionViewModel(
    private val speechRecognizer: SpeechRecognizer
) {
    private val viewModel by lazy {
        SpeechRecognitionViewModel(
            speechRecognizer = speechRecognizer
        )
    }
    val state = viewModel.uiState

    fun requestAudioPermission() {
        viewModel.requestAudioPermission()
    }

    fun initRecognizer() {
        viewModel.initRecognizer()
    }
    fun finishRecognizer(){
        viewModel.finishRecognizer()
    }
    fun startRecognizer() {
        viewModel.startRecognizer { isFinal, text ->
                formatText(text.trim())
        }
    }

    private fun formatText(text: String): String {
        val chunks = mutableListOf<String>()
        var remaining = text

        while (remaining.length > 250) {
            val chunk = remaining.take(250)
            val lastDot = chunk.lastIndexOf(".")

            if (lastDot > 0) {
                chunks.add(remaining.substring(0, lastDot + 1))
                remaining = remaining.substring(lastDot + 1)
            } else {
                chunks.add(remaining.substring(0, 250))
                remaining = remaining.substring(250)
            }
        }

        chunks.add(remaining)
        return chunks.joinToString("\n\n")
    }

    fun stopRecognizer() {
        viewModel.stopRecognizer()
    }
    fun summarize(){
        viewModel.summarize()
    }

    fun onCleared() {
        viewModel.onCleared()
    }
}