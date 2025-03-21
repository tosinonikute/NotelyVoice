package com.module.notelycompose.audio.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.audio.presentation.AudioPlayerUiState
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

    // For slider interaction
    var sliderPosition by remember { mutableStateOf<Float?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
            Slider(
                value = sliderPosition?.toFloat() ?: uiState.currentPosition.toFloat(),
                onValueChange = { newValue ->
                    sliderPosition = newValue
                },
                onValueChangeFinished = {
                    sliderPosition?.let {
                        onSeekTo(it.toInt())
                        sliderPosition = null
                    }
                },
                valueRange = 0f..uiState.duration.toFloat().coerceAtLeast(1f),
                modifier = Modifier
                    .weight(1f)
                    .height(12.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF4CAF50),
                    activeTrackColor = Color(0xFF4CAF50),
                    inactiveTrackColor = Color.LightGray
                ),
                enabled = uiState.isLoaded
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
