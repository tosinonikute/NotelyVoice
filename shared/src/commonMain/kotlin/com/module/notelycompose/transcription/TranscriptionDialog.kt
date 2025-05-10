package com.module.notelycompose.transcription

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.audio.ui.recorder.recordingUiComponentBackButton
import com.module.notelycompose.getPlatform
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.resources.vectors.IcChevronLeft
import com.module.notelycompose.resources.vectors.IcRecorder
import com.module.notelycompose.resources.vectors.Images
import notelycompose.shared.generated.resources.Res
import notelycompose.shared.generated.resources.note_detail_recorder
import notelycompose.shared.generated.resources.top_bar_back
import notelycompose.shared.generated.resources.transcription_dialog_append
import notelycompose.shared.generated.resources.transcription_dialog_original
import notelycompose.shared.generated.resources.transcription_dialog_summarize
import org.jetbrains.compose.resources.stringResource


@Composable
fun TranscriptionDialog(
    modifier: Modifier,
    transcriptionUiState: TranscriptionUiState,
    onAskingForAudioPermission: () -> Unit,
    onRecognitionInitialized: () -> Unit,
    onRecognitionFinished: () -> Unit,
    onRecognitionStart:()->Unit,
    onRecognitionStopped:()->Unit,
    onAppendContent:(String)->Unit,
    onSummarizeContent:()->Unit,
    onDismiss:()->Unit
) {

    val scrollState = rememberScrollState()
    LaunchedEffect(transcriptionUiState.originalText) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }
    DisposableEffect(Unit) {
        onAskingForAudioPermission()
        onRecognitionInitialized()
        onDispose {
            onRecognitionFinished()
        }
    }
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            backgroundColor = LocalCustomColors.current.bodyBackgroundColor
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 48.dp)
            ) {
                Box(modifier = Modifier.align(Alignment.Start).padding(4.dp)) {
                    BackButton(onNavigateBack = {
                            onRecognitionStopped()
                            onRecognitionFinished()
                            onDismiss()
                        }
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .border(
                            2.dp,
                            LocalCustomColors.current.bodyContentColor,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                ) {
                    Text(
                        if(transcriptionUiState.viewOriginalText) transcriptionUiState.originalText else transcriptionUiState.summarizedText,
                        color = LocalCustomColors.current.bodyContentColor
                    )
                }
                FloatingActionButton(
                    modifier = Modifier.padding(vertical = 8.dp),
                    shape = CircleShape,
                    onClick = {
                        if (!transcriptionUiState.isListening) {
                            onRecognitionStart()
                        } else {
                            onRecognitionStopped()
                        }
                    },
                    backgroundColor = if (transcriptionUiState.isListening) Color.Red else Color.Green
                ) {
                    Icon(
                        imageVector = Images.Icons.IcRecorder,
                        contentDescription = stringResource(Res.string.note_detail_recorder),
                        tint = LocalCustomColors.current.bodyContentColor
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp),
                        shape = RoundedCornerShape(4.dp),
                        content = {
                            Text(
                                stringResource(Res.string.transcription_dialog_append),
                                color = LocalCustomColors.current.bodyContentColor
                            )
                        }, onClick = {
                            onAppendContent(if(transcriptionUiState.viewOriginalText) transcriptionUiState.originalText else transcriptionUiState.summarizedText)
                        })
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp),
                        shape = RoundedCornerShape(4.dp),
                        content = {
                            Text(
                                if(transcriptionUiState.viewOriginalText) stringResource(Res.string.transcription_dialog_summarize) else
                                    stringResource(Res.string.transcription_dialog_original),
                                fontSize = 12.sp,
                                color = LocalCustomColors.current.bodyContentColor
                            )
                        }, onClick = {
                            onSummarizeContent()
                        })
                }

            }
        }

}
@Composable
fun BackButton(
    onNavigateBack: () -> Unit
) {
    if (getPlatform().isAndroid) {
        IconButton(
            onClick = onNavigateBack,
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(Res.string.top_bar_back),
                tint = LocalCustomColors.current.bodyContentColor
            )
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onNavigateBack() }
        ) {
            androidx.compose.material.Icon(
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





