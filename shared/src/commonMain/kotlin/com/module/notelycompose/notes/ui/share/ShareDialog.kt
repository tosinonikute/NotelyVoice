package com.module.notelycompose.notes.ui.share

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.module.notelycompose.notes.ui.theme.LocalCustomColors

@Composable
fun ShareDialog(
    onShareAudioRecording: () -> Unit,
    onShareTexts: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Share Options",
                style = MaterialTheme.typography.h6
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onShareAudioRecording,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = LocalCustomColors.current.shareDialogButtonColor,
                        contentColor = LocalCustomColors.current.bodyBackgroundColor
                    )
                ) {
                    Text("Share Audio Recording")
                }

                Button(
                    onClick = onShareTexts,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = LocalCustomColors.current.shareDialogButtonColor,
                        contentColor = LocalCustomColors.current.bodyBackgroundColor
                    )
                ) {
                    Text("Share Texts")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = LocalCustomColors.current.shareDialogBackgroundColor,
                    contentColor = LocalCustomColors.current.bodyContentColor
                )
            ) {
                Text(
                    text = "Close"
                )
            }
        },
        backgroundColor = LocalCustomColors.current.shareDialogBackgroundColor,
        contentColor = LocalCustomColors.current.bodyContentColor
    )
}
