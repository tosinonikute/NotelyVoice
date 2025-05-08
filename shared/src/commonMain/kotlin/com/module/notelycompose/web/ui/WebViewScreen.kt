package com.module.notelycompose.web.ui

import com.module.notelycompose.web.WebViewContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.module.notelycompose.getPlatform
import com.module.notelycompose.notes.ui.detail.AndroidNoteTopBar
import com.module.notelycompose.notes.ui.detail.IOSNoteTopBar

@Composable
fun WebViewScreen(
    title: String,
    url: String,
    onBackPressed: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (getPlatform().isAndroid) {
            AndroidNoteTopBar(
                title = title,
                onNavigateBack = onBackPressed
            )
        } else {
            IOSNoteTopBar(
                onNavigateBack = onBackPressed
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            WebViewContent(
                url = url
            )
        }
    }
}
