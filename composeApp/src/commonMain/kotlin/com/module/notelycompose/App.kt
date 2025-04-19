package com.module.notelycompose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun App() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Lorem Ipsum", modifier = Modifier.align(Alignment.Center))
    }
}