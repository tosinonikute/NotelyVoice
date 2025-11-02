package com.module.notelycompose.audio.ui.importing

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.audio.ui.recorder.RecordingSuccessScreen
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.resources.Res
import com.module.notelycompose.resources.import_failed_title
import com.module.notelycompose.resources.ok
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ImportingAudioStateHost(
    state: ImportingAudioState,
    onSuccess: (String) -> Unit,
    onRelease: () -> Unit,
) {
    when (state) {
        is ImportingAudioState.Idle -> Unit // No UI needed

        is ImportingAudioState.Importing -> {
            ImportingCircularProgressIndicator(
                percentage = state.progress
            )
        }

        is ImportingAudioState.Success -> {
            RecordingSuccessScreen()
            LaunchedEffect(Unit) {
                delay(2000)
                onSuccess(state.path)
                onRelease()
            }
        }

        is ImportingAudioState.Failure -> {
            AlertDialog(
                onDismissRequest = onRelease,
                confirmButton = {
                    TextButton(onClick = onRelease) {
                        Text(stringResource(Res.string.ok))
                    }
                },
                title = { Text(stringResource(Res.string.import_failed_title)) },
                text = { Text(state.message) }
            )
        }
    }
}


@Composable
private fun ImportingCircularProgressIndicator(
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