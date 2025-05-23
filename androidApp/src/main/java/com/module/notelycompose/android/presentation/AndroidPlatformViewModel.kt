package com.module.notelycompose.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.notelycompose.Platform
import com.module.notelycompose.platform.presentation.PlatformViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidPlatformViewModel @Inject constructor(
    private val platformInfo: Platform
) : ViewModel() {

    private val viewModel by lazy {
        PlatformViewModel(
            platformInfo = platformInfo,
            coroutineScope = viewModelScope
        )
    }
    val state = viewModel.state
}
