package com.module.notelycompose

import com.module.notelycompose.platform.presentation.PlatformViewModel

class IOSPlatformViewModel (
    private val platformInfo: Platform
) {
    private val viewModel by lazy {
        PlatformViewModel(
            platformInfo = platformInfo,
            coroutineScope = null
        )
    }
    val state = viewModel.state
}
