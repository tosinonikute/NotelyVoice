package com.module.notelycompose.notes.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import notelycompose.shared.generated.resources.Res
import notelycompose.shared.generated.resources.top_bar_notes
import org.jetbrains.compose.resources.stringResource

@Composable
fun TopBar(
    title: String = stringResource(Res.string.top_bar_notes),
    isLeftIconVisible: Boolean = true,
    isRightIconVisible: Boolean = true,
    onMenuClicked: () -> Unit = {},
    onSettingsClicked: () -> Unit = {}
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
                        .background(LocalCustomColors.current.bodyContentColor)
                        .align(Alignment.CenterStart)
                        .clickable { onMenuClicked() },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        tint = LocalCustomColors.current.bodyBackgroundColor,
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
                    imageVector = Icons.Outlined.Settings,
                    tint = LocalCustomColors.current.settingsIconColor,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(24.dp)
                        .clickable { onSettingsClicked() },
                    contentDescription = ""
                )
            }
        }
    }
}
