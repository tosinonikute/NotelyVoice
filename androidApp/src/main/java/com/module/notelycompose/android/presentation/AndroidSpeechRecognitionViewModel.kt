package com.module.notelycompose.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.notelycompose.audio.presentation.SpeechRecognitionViewModel
import com.module.notelycompose.audio.ui.expect.SpeechRecognizer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidSpeechRecognitionViewModel @Inject constructor(
    private val speechRecognizer: SpeechRecognizer
) : ViewModel() {

    private val viewModel by lazy {
        SpeechRecognitionViewModel(
            speechRecognizer = speechRecognizer,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.uiState


    fun onStartRecognizing(filePath:String) {
        viewModel.onStartRecognizing(filePath){
            "${viewModel.uiState.value.text.trim()}\n${it.trim()}"
        }
    }

    fun finishRecognizer(){
        viewModel.finishRecognizer()
    }
    fun summarize(){
        viewModel.summarize()
    }


    override fun onCleared() {
        viewModel.onCleared()
    }

}
