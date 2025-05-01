package com.module.notelycompose.audio.presentation

import com.module.notelycompose.audio.ui.expect.SpeechRecognizer
import com.module.notelycompose.transcription.TranscriptionUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val RECORD_COUNTER_START = "00:00"
private const val SECONDS_IN_MINUTE = 60
private const val LEADING_ZERO_THRESHOLD = 10
private const val INITIAL_SECOND = 0



class SpeechRecognitionViewModel(
    private val speechRecognizer: SpeechRecognizer,
    coroutineScope: CoroutineScope? = null
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _uiState = MutableStateFlow(TranscriptionUiState())
    val uiState: StateFlow<TranscriptionUiState> = _uiState


    fun onStartRecognizing(filePath: String, onUpdate:(String)->String) {
        _uiState.update { current ->
            current.copy(isLoading = true, recordingPath = filePath)
        }
        viewModelScope.launch {
            speechRecognizer.setup()
            delay(2000)
            speechRecognizer.recognizeFile(filePath) { text ->
                _uiState.update { current ->
                    current.copy(isLoading = false, text = onUpdate(text?:""))
                }
            }
        }
    }

     fun finishRecognizer() {
         _uiState.update { current ->
             current.copy(text = "")
         }
        viewModelScope.launch {
            println("Inside finish recognize")
            speechRecognizer.tearDown()
        }

    }

    fun onCleared() {
        finishRecognizer()
    }
}
