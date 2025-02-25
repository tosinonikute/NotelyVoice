package com.module.notelycompose.notes.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.compositionLocalOf

val DarkCustomColors = CustomColors(
    sortAscendingIconColor = Color(0xFF8514CB),
    backgroundViewColor = Color(0xFF181818),
    dateContentColorViewColor = Color.White,
    dateContentIconColor = Color.White,
    bottomBarBackgroundColor = Color.White,
    bottomBarIconColor = Color(0xFF8514CB),
    noteListBackgroundColor = Color(0xFFEEEEEE),
    bodyBackgroundColor = Color(0xFF181818),
    bodyContentColor = Color.White,
    contentTopColor = Color.White,
    floatActionButtonBorderColor = Color.White,
    floatActionButtonIconColor = Color.White,
    searchOutlinedTextFieldColor = Color.White,
    topButtonIconColor = Color.White,
    noteTextColor = Color.Black,
    noteIconColor = Color.Black,
    iOSBackButtonColor = Color(0xFF3074F6),
    transparentColor = Color.Transparent,
    bottomFormattingContainerColor = Color(0xFFF2F2F2),
    bottomFormattingContentColor = Color.Black
)

val LightCustomColors = CustomColors(
    sortAscendingIconColor = Color(0xFFA260CC),
    backgroundViewColor = Color(0xFFFFFFFF),
    dateContentColorViewColor = Color.Black,
    dateContentIconColor = Color.Black,
    bottomBarBackgroundColor = Color(0xFFFFFFFF),
    bottomBarIconColor = Color.White,
    noteListBackgroundColor = Color(0xFFF4E7F9),
    bodyBackgroundColor = Color(0xFFFFFFFF),
    contentTopColor = Color.Black,
    bodyContentColor = Color.Black,
    floatActionButtonBorderColor = Color.Black,
    floatActionButtonIconColor = Color.Black,
    searchOutlinedTextFieldColor = Color.Black,
    topButtonIconColor = Color.Black,
    noteTextColor = Color.White,
    noteIconColor = Color.White,
    iOSBackButtonColor = Color(0xFF3074F6),
    transparentColor = Color.Transparent,
    bottomFormattingContainerColor = Color(0xFFF2F2F2),
    bottomFormattingContentColor = Color.Black
)

// Create a CompositionLocal to hold the custom colors
val LocalCustomColors = compositionLocalOf {
    LightCustomColors // Default value for custom colors
}
