package com.module.notelycompose.notes.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.module.notelycompose.getPlatform
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.resources.vectors.IcChevronLeft
import com.module.notelycompose.resources.vectors.Images
import notelycompose.composeapp.generated.resources.Res
import notelycompose.composeapp.generated.resources.top_bar_back
import org.jetbrains.compose.resources.stringResource

@Composable
fun DetailNoteTopBar(
    title: String = "My Note",
    onNavigateBack: () -> Unit
) {
    if (getPlatform().isAndroid) {
        AndroidNoteTopBar(
            title = title,
            onNavigateBack = onNavigateBack
        )
    } else {
        IOSNoteTopBar(
            onNavigateBack = onNavigateBack
        )
    }
}

@Composable
private fun AndroidNoteTopBar(
    title: String,
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.top_bar_back)
                )
            }
        },
        backgroundColor = LocalCustomColors.current.bodyBackgroundColor,
        contentColor = LocalCustomColors.current.bodyContentColor
    )
}

@Composable
private fun IOSNoteTopBar(
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    onNavigateBack()
                }
            ) {
                Icon(
                    imageVector = Images.Icons.IcChevronLeft,
                    contentDescription = stringResource(Res.string.top_bar_back),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(Res.string.top_bar_back),
                    style = MaterialTheme.typography.body1,
                )
            }
        },
        contentColor = LocalCustomColors.current.iOSBackButtonColor,
        backgroundColor = LocalCustomColors.current.transparentColor,
        modifier = Modifier.padding(start = 0.dp),
        elevation = 0.dp,
    )
}
