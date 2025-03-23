package com.module.notelycompose.resources.style

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object LayoutGuide {

    // MARK: - Fonts
    object FontSize {
        val small = 14.sp
        val smallPlusPlus = 16.sp
        val medium = 18.sp
    }

    // MARK: - Padding
    object Padding {
        val none = 0.dp
        val extraExtraSmall = 2.dp
        val extraSmall = 4.dp
        val small = 8.dp
        val medium = 16.dp
        val large = 24.dp
        val extraLarge = 32.dp
        val extraExtraLarge = 40.dp
    }

    // MARK: - Platform Audio Player Ui
    object PlatformAudio {
        val playContainerWeight = 0.08f
        val playTimeContainerWeight = 0.10f
        val sliderContainerWeight = 0.6f
        val durationContainerWeight = 0.14f
    }
}
