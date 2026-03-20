package com.module.notelycompose.modelDownloader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.notelycompose.platform.Transcriber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MoveModelUiState(
    val modelFileName: String = "",
    val isMoving: Boolean = false,
    val errorMessage: String? = null
)

sealed class MoveModelEffect {
    data class Success(val message: String) : MoveModelEffect()
    data class Error(val message: String) : MoveModelEffect()
}

class MoveModelViewModel(
    private val transcriber: Transcriber,
    private val modelSelection: ModelSelection
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoveModelUiState())
    val uiState: StateFlow<MoveModelUiState> = _uiState

    private val _effects = MutableSharedFlow<MoveModelEffect>()
    val effects: SharedFlow<MoveModelEffect> = _effects

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedModel = modelSelection.getSelectedModel()
            _uiState.update { it.copy(modelFileName = selectedModel.name) }
        }
    }

    fun moveModelToProtectedStorage() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isMoving = true, errorMessage = null) }

            val result = transcriber.moveModelToProtectedStorage(uiState.value.modelFileName)

            _uiState.update { it.copy(isMoving = false) }

            if (result.isSuccess) {
                _effects.emit(MoveModelEffect.Success("Model moved to protected storage successfully"))
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Unknown error occurred"
                _effects.emit(MoveModelEffect.Error(errorMsg))
                _uiState.update { it.copy(errorMessage = errorMsg) }
            }
        }
    }
}
