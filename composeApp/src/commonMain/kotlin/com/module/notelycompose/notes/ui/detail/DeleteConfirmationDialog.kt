package com.module.notelycompose.notes.ui.detail

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import notelycompose.composeapp.generated.resources.Res
import notelycompose.composeapp.generated.resources.confirmation
import notelycompose.composeapp.generated.resources.confirmation_text
import notelycompose.composeapp.generated.resources.confirmation_delete
import notelycompose.composeapp.generated.resources.confirmation_cancel
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeleteConfirmationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(Res.string.confirmation)) },
            text = { Text(stringResource(Res.string.confirmation_text)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                        onConfirm()
                    }
                ) {
                    Text(stringResource(Res.string.confirmation_delete))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(stringResource(Res.string.confirmation_cancel))
                }
            }
        )
    }
}
