package com.module.notelycompose.audio.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import audio.FileManager
import com.module.notelycompose.audio.ui.importing.ImportingAudioState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AudioImportViewModel(
    private val fileManager: FileManager
) : ViewModel() {

    private var _importingState = MutableStateFlow<ImportingAudioState>(ImportingAudioState.Idle)
    val importingAudioState: StateFlow<ImportingAudioState> = _importingState

    internal fun importAudio() = fileManager.launchAudioPicker {
        viewModelScope.launch {
            _importingState.update { ImportingAudioState.Importing() }

            val path = fileManager.processPickedAudioToWav(
                onProgress = { progress ->
                    _importingState.update { ImportingAudioState.Importing(progress) }
                }
            )

            if (path == null) {
                _importingState.update { ImportingAudioState.Failure("Failed to import audio") }
                return@launch
            }

            Napier.d { "✅ Imported audio path: $path" }
            _importingState.update { ImportingAudioState.Success(path) }
        }
    }

    internal fun importVideo() = fileManager.launchVideoPicker {
        viewModelScope.launch {
            _importingState.update { ImportingAudioState.Importing() }

            val path = fileManager.processPickedVideoToWav(
                onProgress = { progress ->
                    _importingState.update { ImportingAudioState.Importing(progress) }
                }
            )

            if (path == null) {
                _importingState.update { ImportingAudioState.Failure("Failed to import video") }
                return@launch
            }

            Napier.d { "✅ Imported video path: $path" }
            _importingState.update { ImportingAudioState.Success(path) }
        }
    }

    internal fun releaseState() = viewModelScope.launch {
        _importingState.update { ImportingAudioState.Idle }
    }
}
