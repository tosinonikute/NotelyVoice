package com.module.notelycompose.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.notelycompose.audio.presentation.AudioRecorderPresentationState
import com.module.notelycompose.audio.presentation.AudioRecorderViewModel
import com.module.notelycompose.audio.presentation.mappers.AudioRecorderPresentationToUiMapper
import com.module.notelycompose.audio.ui.expect.AudioRecorder
import com.module.notelycompose.audio.ui.recorder.AudioRecorderUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidAudioRecorderViewModel @Inject constructor(
    private val audioRecorder: AudioRecorder,
    private val mapper: AudioRecorderPresentationToUiMapper
) : ViewModel() {

    private val viewModel by lazy {
        AudioRecorderViewModel(
            audioRecorder = audioRecorder,
            mapper = mapper,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.audioRecorderPresentationState

    fun onGetUiState(presentationState: AudioRecorderPresentationState): AudioRecorderUiState {
        return viewModel.onGetUiState(presentationState)
    }

    fun onStartRecording() {
        viewModel.onStartRecording()
    }

    fun onStopRecording() {
        viewModel.onStopRecording()
    }

    fun onRequestAudioPermission() {
        viewModel.onRequestAudioPermission()
    }

    override fun onCleared() {
        viewModel.onCleared()
    }
}
