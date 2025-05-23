package com.module.notelycompose.platform.presentation

import com.module.notelycompose.Platform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlatformViewModel (
    private val platformInfo: Platform,
    coroutineScope: CoroutineScope? = null
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(PlatformUiState())
    val state: StateFlow<PlatformUiState> = _state

    init {
        loadAppInfo()
    }

    private fun loadAppInfo() {
        _state.value = _state.value.copy(
            appVersion = platformInfo.appVersion,
            platformName = platformInfo.name,
            isAndroid = platformInfo.isAndroid
        )
    }
}

data class PlatformUiState(
    val appVersion: String = "",
    val platformName: String = "",
    val isAndroid: Boolean = false
)