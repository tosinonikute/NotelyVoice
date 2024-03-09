package com.module.notelycompose.notes.presentation.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(
    title: String = "Notely",
    isLeftIconVisible: Boolean = true,
    isRightIconVisible: Boolean = true
) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLeftIconVisible) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .align(Alignment.CenterStart),
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp).align(Alignment.Center),
                    contentDescription = ""
                )
            }
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp
        )

        if (isRightIconVisible) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                tint = Color.Black,
                modifier = Modifier.align(Alignment.CenterEnd).size(24.dp),
                contentDescription = ""
            )
        }
    }
}