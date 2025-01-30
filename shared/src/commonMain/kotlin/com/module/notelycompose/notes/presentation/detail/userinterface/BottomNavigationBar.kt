package com.module.notelycompose.notes.presentation.detail.userinterface

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.module.notelycompose.notes.presentation.theme.LocalCustomColors
import com.module.notelycompose.resources.vectors.IcDetailList
import com.module.notelycompose.resources.vectors.IcHeart
import com.module.notelycompose.resources.vectors.IcKeyboardHide
import com.module.notelycompose.resources.vectors.IcLetterAa
import com.module.notelycompose.resources.vectors.Images

@Composable
fun BottomNavigationBar() {
    var showFormatBar by remember { mutableStateOf(false) }
    var selectedFormat by remember { mutableStateOf(TextFormat.Body) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AnimatedVisibility(
            visible = showFormatBar,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            FormatBar(
                selectedFormat = selectedFormat,
                onFormatSelected = { selectedFormat = it },
                onClose = { showFormatBar = false }
            )
        }
    }

    if (!showFormatBar) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalCustomColors.current.bodyBackgroundColor)
                .padding(8.dp)
                .padding(start = 8.dp, end = 48.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                showFormatBar = true
            }) {
                Icon(
                    imageVector = Images.Icons.IcLetterAa,
                    contentDescription = "Letter",
                    tint = LocalCustomColors.current.bodyContentColor
                )
            }
            IconButton(onClick = { /* Delete */ }) {
                Icon(
                    imageVector = Images.Icons.IcDetailList,
                    contentDescription = "Type",
                    tint = LocalCustomColors.current.bodyContentColor
                )
            }
            IconButton(onClick = { /* Type */ }) {
                Icon(
                    imageVector = Images.Icons.IcHeart,
                    contentDescription = "Type",
                    tint = LocalCustomColors.current.bodyContentColor
                )
            }
            IconButton(onClick = { /* Undo */ }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Undo",
                    tint = LocalCustomColors.current.bodyContentColor
                )
            }
            IconButton(onClick = { /* Redo */ }) {
                Icon(
                    imageVector = Images.Icons.IcKeyboardHide,
                    contentDescription = "Redo",
                    tint = LocalCustomColors.current.bodyContentColor
                )
            }
        }
    }

}
