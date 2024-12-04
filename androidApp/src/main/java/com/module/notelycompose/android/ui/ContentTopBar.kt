package com.module.notelycompose.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.sharp.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.android.theme.LocalCustomColors

@Composable
fun ContentTopBar(
    title: String = "Notes",
    isLeftIconVisible: Boolean = true
) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier.fillMaxWidth()
            .background(LocalCustomColors.current.bodyBackgroundColor)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .background(LocalCustomColors.current.bodyBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            if (isLeftIconVisible) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(LocalCustomColors.current.backgroundViewColor)
                        .align(Alignment.CenterStart),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        tint = LocalCustomColors.current.topButtonIconColor,
                        modifier = Modifier.size(24.dp).align(Alignment.Center),
                        contentDescription = ""
                    )
                }
            }
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 48.dp),
                text = title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = LocalCustomColors.current.contentTopColor
            )
        }
    }
}