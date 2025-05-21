package com.module.notelycompose

import com.module.notelycompose.audio.presentation.AudioRecorderPresentationState
import com.module.notelycompose.audio.presentation.AudioRecorderViewModel
import com.module.notelycompose.audio.presentation.mappers.AudioRecorderPresentationToUiMapper
import com.module.notelycompose.audio.ui.expect.AudioRecorder
import com.module.notelycompose.audio.ui.recorder.AudioRecorderUiState

class IOSAudioRecorderViewModel(
    private val audioRecorder: AudioRecorder,
    private val mapper: AudioRecorderPresentationToUiMapper
) {
    private val viewModel by lazy {
        AudioRecorderViewModel(
            audioRecorder = audioRecorder,
            mapper = mapper
        )
    }
    val state = viewModel.audioRecorderPresentationState

    fun onGetUiState(presentationState: AudioRecorderPresentationState): AudioRecorderUiState {
        return viewModel.onGetUiState(presentationState)
    }

    fun onStartRecording(updateUI:()->Unit) {
        viewModel.onStartRecording(updateUI)
    }

    fun onStopRecording() {
        viewModel.onStopRecording()
    }

    suspend fun setupRecorder() {
        viewModel.setupRecorder()
    }

    suspend fun finishRecorder() {
        viewModel.finishRecorder()
    }

    fun onPauseRecording() {
        viewModel.onPauseRecording()
    }

    fun onResumeRecording() {
        viewModel.onResumeRecording()
    }

    fun onRequestAudioPermission() {
        viewModel.onRequestAudioPermission()
    }

    fun onCleared() {
        viewModel.onCleared()
    }
}