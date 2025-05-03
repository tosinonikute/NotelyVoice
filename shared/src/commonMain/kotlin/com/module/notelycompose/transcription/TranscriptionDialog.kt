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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import notelycompose.shared.generated.resources.Res
import notelycompose.shared.generated.resources.transcription_dialog_append
import notelycompose.shared.generated.resources.transcription_dialog_cancel
import notelycompose.shared.generated.resources.transcription_dialog_summarize
import org.jetbrains.compose.resources.stringResource


@Composable
fun TranscriptionDialog(
    modifier: Modifier,
    transcriptionUiState: TranscriptionUiState,
    onRecognitionStart:()->Unit,
    onRecognitionStopped:()->Unit,
    onAppendContent:(String)->Unit,
    onSummarizeContent:()->Unit,
    onDismiss:()->Unit
) {

    val scrollState = rememberScrollState()
    LaunchedEffect(transcriptionUiState.summarizedText) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }
    DisposableEffect(Unit){
        onRecognitionStart()
        onDispose {
            onRecognitionStopped()
        }

    }
    Dialog(
        properties = DialogProperties(
            dismissOnClickOutside = true
        ),
        onDismissRequest = {onDismiss()}) {
        Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = LocalCustomColors.current.bodyBackgroundColor
        ) {
                Column(modifier = Modifier.padding(8.dp)) {
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
                            transcriptionUiState.summarizedText,
                            color = LocalCustomColors.current.bodyContentColor
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
                                    stringResource(Res.string.transcription_dialog_summarize),
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
                            onDismiss()
                        })
                }
        }
    }
}





