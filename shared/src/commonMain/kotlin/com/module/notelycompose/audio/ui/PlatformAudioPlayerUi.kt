package com.module.notelycompose.audio.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.audio.presentation.AudioPlayerUiState
import com.module.notelycompose.audio.ui.uicomponents.Thumb
import com.module.notelycompose.audio.ui.uicomponents.Track
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.resources.vectors.IcPause
import com.module.notelycompose.resources.vectors.Images

@Composable
fun PlatformAudioPlayerUi(
    filePath: String,
    uiState: AudioPlayerUiState,
    onLoadAudio: (filePath: String) -> Unit,
    onClear: () -> Unit,
    onSeekTo: (position: Int) -> Unit,
    onTogglePlayPause: () -> Unit
) {
    // Load audio when the composable is first created
    LaunchedEffect(filePath) {
        onLoadAudio(filePath)
    }

    // Dispose of ViewModel resources when the composable leaves composition
    DisposableEffect(uiState) {
        onDispose {
            onClear()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 0.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(LocalCustomColors.current.playerBoxBackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Play/Pause button
            IconButton(
                onClick = { onTogglePlayPause() },
                modifier = Modifier.size(28.dp),
                enabled = uiState.isLoaded
            ) {
                Icon(
                    imageVector = if (uiState.isPlaying) Images.Icons.IcPause else Icons.Filled.PlayArrow,
                    contentDescription = if (uiState.isPlaying) "Pause" else "Play",
                    modifier = Modifier.size(28.dp),
                    tint = if (uiState.isLoaded) Color.DarkGray else Color.LightGray
                )
            }

            // Current time
            Text(
                text = uiState.currentPosition.formatTimeToMMSS(),
                color = Color.DarkGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Progress slider
            AudioSlider(
                modifier = Modifier.padding(2.dp),
                filePath = filePath,
                uiState = uiState,
                onLoadAudio = { filePath -> onLoadAudio(filePath) },
                onClear = { onClear() },
                onSeekTo = { position -> onSeekTo(position) },
                onTogglePlayPause = { onTogglePlayPause() }
            )

            // Total duration
            Text(
                text = uiState.duration.formatTimeToMMSS(),
                color = Color.DarkGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioSlider(
    modifier: Modifier = Modifier,
    filePath: String,
    uiState: AudioPlayerUiState,
    onLoadAudio: (filePath: String) -> Unit,
    onClear: () -> Unit,
    onSeekTo: (position: Int) -> Unit,
    onTogglePlayPause: () -> Unit
) {
    var sliderPosition by remember { mutableStateOf<Float?>(null) }
    var value by remember { mutableFloatStateOf(0f) }
    val interactionSource = remember { MutableInteractionSource() }

    Column {
        Slider(
            value = sliderPosition?.toFloat() ?: uiState.currentPosition.toFloat(),
            onValueChange = {
                value = it
            },
            modifier = modifier,
            thumb = {
                Thumb(
                    interactionSource = interactionSource,
                    colors = SliderDefaults.colors(
                        thumbColor = LocalCustomColors.current.activeThumbTrackColor,
                    ),
                    thumbSize = DpSize(width = 16.dp, height = 16.dp)
                )
            },
            track = { state ->
                Track(
                    sliderState = state,
                    colors = SliderDefaults.colors(
                        activeTrackColor = LocalCustomColors.current.activeThumbTrackColor,
                        inactiveTrackColor = Color.LightGray
                    )
                )
            },
            onValueChangeFinished = {
                sliderPosition?.let {
                    onSeekTo(it.toInt())
                    sliderPosition = null
                }
            },
            valueRange = 0f..uiState.duration.toFloat().coerceAtLeast(1f),
            enabled = uiState.isLoaded,
            interactionSource = interactionSource
        )
    }
}
