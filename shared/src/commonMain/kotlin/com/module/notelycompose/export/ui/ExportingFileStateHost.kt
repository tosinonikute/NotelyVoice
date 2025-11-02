package com.module.notelycompose.export.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.audio.ui.recorder.RecordingSuccessScreen
import com.module.notelycompose.export.presentation.model.ExportingFileState
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.resources.Res
import com.module.notelycompose.resources.batch_export_settings_error_occurred
import com.module.notelycompose.resources.batch_export_settings_no_folder_selected
import com.module.notelycompose.resources.batch_export_ok
import com.module.notelycompose.resources.error
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ExportingFileStateHost(
    state: ExportingFileState,
    onDismiss: () -> Unit
) {
    var shouldShowDialog by remember { mutableStateOf(true) }
    when (state) {
        is ExportingFileState.Idle -> Unit

        is ExportingFileState.Exporting -> {
            ExportingCircularProgressIndicator(
                percentage = state.progress
            )
        }

        is ExportingFileState.Success -> {
            RecordingSuccessScreen()
            LaunchedEffect(Unit) {
                delay(2000)
                onDismiss()
            }
        }

        is ExportingFileState.Failure -> {
            if(shouldShowDialog) {
                AlertDialog(
                    onDismissRequest = onDismiss,
                    confirmButton = {
                        TextButton(onClick = {
                            shouldShowDialog = false
                            onDismiss()
                        }) {
                            Text(stringResource(Res.string.batch_export_ok))
                        }
                    },
                    title = { Text(stringResource(Res.string.error)) },
                    text = { Text(stringResource(Res.string.batch_export_settings_error_occurred)) }
                )
            }
        }

        is ExportingFileState.NoFolderSelected -> {
            if(shouldShowDialog) {
                AlertDialog(
                    onDismissRequest = onDismiss,
                    confirmButton = {
                        TextButton(onClick = {
                            shouldShowDialog = false
                            onDismiss()
                        }) {
                            Text(stringResource(Res.string.batch_export_ok))
                        }
                    },
                    title = { Text(stringResource(Res.string.error)) },
                    text = { Text(stringResource(Res.string.batch_export_settings_no_folder_selected)) }
                )
            }
        }
    }
}


@Composable
private fun ExportingCircularProgressIndicator(
    percentage: Float,
    radius: Dp = 80.dp,
    strokeWidth: Dp = 12.dp,
    contentColor: Color = LocalCustomColors.current.bodyContentColor,
    trackColor: Color = LocalCustomColors.current.bodyContentColor.copy(alpha = 0.2f),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.bodyBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (percentage == 0f) {
            CircularProgressIndicator(
                modifier = Modifier.size(radius.times(2)),
                color = contentColor,
                strokeWidth = strokeWidth,
            )
        } else {
            CircularProgressIndicator(
                progress = { percentage },
                modifier = Modifier.size(radius.times(2)),
                color = contentColor,
                strokeWidth = strokeWidth,
                gapSize = 0.dp,
                trackColor = trackColor,
                strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
            )
        }

        Text(
            text = "${(percentage * 100).toInt()}%",
            fontSize = 20.sp,
            color = contentColor
        )
    }
}
