package com.module.notelycompose.transcription

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.resources.vectors.IcRecorder
import com.module.notelycompose.resources.vectors.Images
import notelycompose.shared.generated.resources.Res
import notelycompose.shared.generated.resources.note_detail_recorder
import notelycompose.shared.generated.resources.transcription_dialog_append
import notelycompose.shared.generated.resources.transcription_dialog_cancel
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
    Dialog(
        properties = DialogProperties(
            dismissOnClickOutside = true
        ),
        onDismissRequest = {
            onRecognitionStopped()
            onRecognitionFinished()
            onDismiss()
        }) {
        Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = LocalCustomColors.current.bodyBackgroundColor
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
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
                            onAppendContent(transcriptionUiState.summarizedText)
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
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp),
                    shape = RoundedCornerShape(4.dp),
                    content = {
                        Text(
                            stringResource(Res.string.transcription_dialog_cancel),
                            color = LocalCustomColors.current.bodyContentColor
                        )
                    }, onClick = {
                        onRecognitionStopped()
                        onRecognitionFinished()
                        onDismiss()
                    })
            }
        }
    }
}





