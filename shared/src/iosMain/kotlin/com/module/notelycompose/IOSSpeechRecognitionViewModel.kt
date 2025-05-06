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
                text.trim()
        }
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