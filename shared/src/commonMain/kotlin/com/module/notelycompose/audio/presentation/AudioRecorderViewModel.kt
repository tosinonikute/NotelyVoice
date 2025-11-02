package com.module.notelycompose.audio.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.notelycompose.audio.domain.AudioRecorderInteractor
import com.module.notelycompose.audio.domain.AudioRecorderPresentationState
import com.module.notelycompose.audio.ui.recorder.AudioRecorderUiState
import com.module.notelycompose.audio.ui.recorder.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AudioRecorderViewModel(
    private val interactor: AudioRecorderInteractor
) : ViewModel() {
    val audioRecorderPresentationState: StateFlow<AudioRecorderPresentationState> =
        interactor.state

    init {
        setupRecorder()
    }
    private val _screenState = MutableStateFlow(ScreenState.Initial)
    val uiState: StateFlow<ScreenState> = _screenState.asStateFlow()

    fun onStartRecording(noteId: Long?) {
        interactor.initState()
        interactor.onStartRecording(noteId,viewModelScope, {
            _screenState.update { ScreenState.Recording }
        })
    }

    fun onStopRecording() = viewModelScope.launch {
        interactor.onStopRecording(this)
        _screenState.update { ScreenState.Success }
    }

    fun setupRecorder() {
        interactor.setupRecorder(viewModelScope)
    }

    fun finishRecorder() {
        interactor.finishRecorder(viewModelScope)
    }

    fun onPauseRecording() {
        interactor.onPauseRecording(viewModelScope)
    }

    fun onResumeRecording() {
        interactor.onResumeRecording(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        interactor.onCleared()
        onStopRecording()
        finishRecorder()
    }

    fun onRequestAudioPermission() {
        interactor.onRequestAudioPermission(viewModelScope)
    }

    fun onGetUiState(presentationState: AudioRecorderPresentationState): AudioRecorderUiState {
        return interactor.onGetUiState(presentationState)
    }
}
