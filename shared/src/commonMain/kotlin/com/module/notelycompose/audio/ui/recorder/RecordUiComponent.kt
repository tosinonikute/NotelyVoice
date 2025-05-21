package com.module.notelycompose.audio.ui.recorder

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.getPlatform
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.resources.vectors.IcChevronLeft
import com.module.notelycompose.resources.vectors.IcPause
import com.module.notelycompose.resources.vectors.IcRecorder
import com.module.notelycompose.resources.vectors.Images
import kotlinx.coroutines.delay
import notelycompose.shared.generated.resources.Res
import notelycompose.shared.generated.resources.recording_ui_microphone
import notelycompose.shared.generated.resources.recording_ui_tap_start_record
import notelycompose.shared.generated.resources.recording_ui_tap_stop_record
import notelycompose.shared.generated.resources.recording_ui_loading
import notelycompose.shared.generated.resources.recording_ui_loading_rotation
import notelycompose.shared.generated.resources.recording_ui_checkmark
import notelycompose.shared.generated.resources.top_bar_back
import notelycompose.shared.generated.resources.transcription_icon
import org.jetbrains.compose.resources.stringResource

enum class ScreenState {
    Initial,
    Recording,
    Success
}

@Composable
fun RecordUiComponent(
    onDismiss: () -> Unit,
    recordCounterString: String,
    onStartRecord: (()->Unit) -> Unit,
    onStopRecord: () -> Unit,
    onAfterRecord: () -> Unit
) {
    var screenState by remember { mutableStateOf(ScreenState.Initial) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.bodyBackgroundColor)
            .windowInsetsPadding(WindowInsets(0))
            .padding(0.dp)
    ) {

        when(screenState) {
            ScreenState.Initial -> RecordingInitialScreen(
                onNavigateBack = onDismiss,
                onTapToRecord = {
                    onStartRecord({
                        screenState = ScreenState.Recording
                    })
                },
                onStopRecording = onStopRecord
            )
            ScreenState.Recording -> RecordingInProgressScreen(
                counterTimeString = recordCounterString,
                onStopRecording = {
                    onStopRecord()
                    screenState = ScreenState.Success
                },
                onNavigateBack = onDismiss
            )
            ScreenState.Success -> {
                RecordingSuccessScreen()
                LaunchedEffect(Unit) {
                    delay(2000)
                    onAfterRecord()
                    onDismiss()
                }
            }

        }
    }
}

@Composable
fun RecordingInitialScreen(
    onNavigateBack: () -> Unit,
    onTapToRecord: () -> Unit,
    onStopRecording: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.bodyBackgroundColor)
    ) {
        recordingUiComponentBackButton(
            onNavigateBack = onNavigateBack,
            onStopRecording = onStopRecording
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 80.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.Green)
                    .clickable { onTapToRecord() },
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Images.Icons.IcRecorder,
                    contentDescription = stringResource(Res.string.recording_ui_microphone),
                    tint = LocalCustomColors.current.bodyBackgroundColor,
                    modifier = Modifier.size(32.dp)
                )
            }

            Text(
                text = stringResource(Res.string.recording_ui_tap_start_record),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = LocalCustomColors.current.bodyContentColor,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun RecordingInProgressScreen(
    counterTimeString: String,
    onNavigateBack: () -> Unit,
    onStopRecording: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.bodyBackgroundColor)
    ) {
        recordingUiComponentBackButton(
            onNavigateBack = onNavigateBack,
            onStopRecording = onStopRecording
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp)
            ) {
                LoadingAnimation()
                Text(
                    text = counterTimeString,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Normal,
                    color = LocalCustomColors.current.bodyContentColor
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp)
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.dp, LocalCustomColors.current.bodyContentColor, CircleShape)
                            .padding(vertical = 2.dp, horizontal = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Images.Icons.IcPause,
                            contentDescription = stringResource(Res.string.transcription_icon),
                            tint = LocalCustomColors.current.bodyContentColor,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {  }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.dp, LocalCustomColors.current.bodyContentColor, CircleShape)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Red)
                                .clickable {
                                    onStopRecording()
                                }
                        )
                    }
                }

                Text(
                    text = stringResource(Res.string.recording_ui_tap_stop_record),
                    color = LocalCustomColors.current.bodyContentColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
        }
    }
}

@Composable
fun LoadingAnimation() {
    val drawArcColor = LocalCustomColors.current.bodyContentColor
    val infiniteTransition = rememberInfiniteTransition(
        label = stringResource(Res.string.recording_ui_loading)
    )
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = stringResource(Res.string.recording_ui_loading_rotation)
    )

    Canvas(modifier = Modifier.size(200.dp)) {
        drawArc(
            color = drawArcColor,
            startAngle = rotation,
            sweepAngle = 300f,
            useCenter = false,
            style = Stroke(width = 4f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun RecordingSuccessScreen() {
    val pathColor = LocalCustomColors.current.bodyContentColor
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.bodyBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        var animationPlayed by remember { mutableStateOf(false) }
        val pathProgress by animateFloatAsState(
            targetValue = if (animationPlayed) 1f else 0f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            label = stringResource(Res.string.recording_ui_checkmark)
        )

        LaunchedEffect(Unit) {
            animationPlayed = true
        }

        Canvas(modifier = Modifier.size(100.dp)) {
            val path = Path().apply {

                addArc(
                    Rect(
                        offset = Offset(0f, 0f),
                        size = Size(size.width, size.height)
                    ),
                    0f,
                    360f * pathProgress
                )

                if (pathProgress > 0.5f) {
                    val checkProgress = (pathProgress - 0.5f) * 2f
                    moveTo(size.width * 0.2f, size.height * 0.5f)
                    lineTo(
                        size.width * 0.45f,
                        size.height * 0.7f * checkProgress
                    )
                    lineTo(
                        size.width * 0.8f,
                        size.height * 0.3f * checkProgress
                    )
                }
            }

            drawPath(
                path = path,
                color = pathColor,
                style = Stroke(
                    width = 8f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}

@Composable
fun recordingUiComponentBackButton(
    onNavigateBack: () -> Unit,
    onStopRecording: () -> Unit
) {
    if (getPlatform().isAndroid) {
        IconButton(
            onClick = {
                onStopRecording()
                onNavigateBack()
            },
            modifier = Modifier.padding(16.dp)
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(Res.string.top_bar_back),
                tint = LocalCustomColors.current.bodyContentColor
            )
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    onStopRecording()
                    onNavigateBack()
                }
        ) {
            Icon(
                imageVector = Images.Icons.IcChevronLeft,
                contentDescription = stringResource(Res.string.top_bar_back),
                modifier = Modifier.size(28.dp),
                tint = LocalCustomColors.current.bodyContentColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            androidx.compose.material.Text(
                text = stringResource(Res.string.top_bar_back),
                style = androidx.compose.material.MaterialTheme.typography.body1,
                color = LocalCustomColors.current.bodyContentColor
            )
        }
    }
}
