package com.module.notelycompose.modelDownloader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.notelycompose.onboarding.data.PreferencesRepository
import com.module.notelycompose.platform.Downloader
import com.module.notelycompose.platform.Transcriber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ModelDownloaderViewModel(
    private val downloader: Downloader,
    private val transcriber: Transcriber,
    private val modelSelection: ModelSelection
):ViewModel(){
    private var _uiState: MutableStateFlow<DownloaderUiState> = MutableStateFlow(DownloaderUiState(modelSelection.getDefaultTranscriptionModel()))

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedModel = modelSelection.getSelectedModel()
            _uiState.value = DownloaderUiState(selectedModel)
        }
    }
    val uiState: StateFlow<DownloaderUiState> = _uiState

    private val _effects = MutableSharedFlow<DownloaderEffect>()
    val effects: SharedFlow<DownloaderEffect> = _effects


    fun checkTranscriptionAvailability() {
        viewModelScope.launch(Dispatchers.IO) {
            _effects.emit(DownloaderEffect.CheckingEffect())
            
            if (downloader.hasRunningDownload()) {
                trackDownload()
            } else {
                if (!transcriber.doesModelExists(uiState.value.selectedModel.name)
                    || !transcriber.isValidModel(uiState.value.selectedModel.name)) {
                    _effects.emit(DownloaderEffect.AskForUserAcceptance())
                } else {
                    _effects.emit(DownloaderEffect.ModelsAreReady())
                }
            }
        }
    }

    fun startDownload() {
        viewModelScope.launch(Dispatchers.IO) {
                val modelUrl = uiState.value.selectedModel.url
//            if (modelUrl != null) {
                downloader.startDownload(modelUrl, uiState.value.selectedModel.name)
                trackDownload()
//            } else {
//                _effects.emit(DownloaderEffect.ErrorEffect())
//            }
        }
    }

    private suspend fun trackDownload() {
        _effects.emit(DownloaderEffect.DownloadEffect())
        downloader.trackDownloadProgress(
            uiState.value.selectedModel.name,
            onProgressUpdated = { progress, downloadedMB, totalMB ->
            _uiState.update { current ->
                current.copy(
                    progress = progress.toFloat(),
                    downloaded = downloadedMB,
                    total = totalMB
                )
            }
        }, onSuccess = {
            viewModelScope.launch {
                transcriber.initialize(uiState.value.selectedModel.name)
                _effects.emit(DownloaderEffect.ModelsAreReady()) }

        }, onFailed = {
            viewModelScope.launch { _effects.emit(DownloaderEffect.ErrorEffect()) }
        })
    }
}
