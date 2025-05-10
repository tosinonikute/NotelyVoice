package com.module.notelycompose.audio.presentation

import com.module.notelycompose.audio.ui.expect.SpeechRecognizer
import com.module.notelycompose.notes.ui.extensions.firstToUpperCase
import com.module.notelycompose.summary.Text2Summary
import com.module.notelycompose.transcription.TranscriptionUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch




class SpeechRecognitionViewModel(
    private val speechRecognizer: SpeechRecognizer,
    coroutineScope: CoroutineScope? = null
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _uiState = MutableStateFlow(TranscriptionUiState())
    val uiState: StateFlow<TranscriptionUiState> = _uiState

    fun requestAudioPermission() {
        viewModelScope.launch {
            speechRecognizer.requestRecordingPermission()
        }
    }

    fun initRecognizer() {
        viewModelScope.launch {
            speechRecognizer.initialize()
        }
        }


    fun startRecognizer(onUpdate: (Boolean, String) -> String) {
        _uiState.update { current ->
            current.copy(
                isListening = true,
                finalText = _uiState.value.originalText,
                partialText = ""
            )
        }
        viewModelScope.launch {
            if (speechRecognizer.hasRecordingPermission()) {
                speechRecognizer.startRecognizing { isFinal, text ->
                    val newText = onUpdate(isFinal, text ?: "")
                    _uiState.update { current ->
                        if (isFinal)
                            current.copy(
                                originalText = newText,
                                finalText = newText,
                                partialText = ""
                            )
                        else
                            current.copy(
                                originalText = "${_uiState.value.finalText}\n\n${newText.firstToUpperCase()}".trim(),
                                partialText = newText
                            )
                    }

                }
            }
        }

    }

    fun stopRecognizer() {
        _uiState.update { current ->
            current.copy(isListening = false)
        }
        viewModelScope.launch {
            speechRecognizer.stopRecognizer()
        }

    }

    fun finishRecognizer() {
        _uiState.update { current ->
            current.copy(
                isListening = false,
                originalText = "",
                finalText = "",
                partialText = "",
                summarizedText = ""
            )
        }
        viewModelScope.launch {
            speechRecognizer.finishRecognizer()
        }
    }

    fun summarize() {
        if (_uiState.value.viewOriginalText) {
            viewModelScope.launch {
                val summarizedText = Text2Summary.summarize(_uiState.value.originalText, 0.7f)
                _uiState.update { current ->
                    current.copy(viewOriginalText = false, summarizedText = summarizedText)
                }

            }
        } else {
            _uiState.update { current ->
                current.copy(viewOriginalText = true)
            }
        }

    }

    fun onCleared() {
        stopRecognizer()
    }
}
