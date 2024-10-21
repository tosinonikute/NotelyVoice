package com.module.notelycompose.android.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.compositionLocalOf

val LightCustomColors = CustomColors(
    sortAscendingIconColor = Color(0xFFA260CC)
)

val DarkCustomColors = CustomColors(
    sortAscendingIconColor = Color(0xFFA260CC)
)

// Create a CompositionLocal to hold the custom colors
val LocalCustomColors = compositionLocalOf {
    LightCustomColors // Default value for custom colors
}
