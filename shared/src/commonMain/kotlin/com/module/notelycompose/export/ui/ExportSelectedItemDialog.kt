package com.module.notelycompose.export.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.resources.Res
import com.module.notelycompose.resources.batch_export_audio_files
import com.module.notelycompose.resources.batch_export_options
import com.module.notelycompose.resources.batch_export_select_at_least_one_option
import com.module.notelycompose.resources.batch_export_select_formats
import com.module.notelycompose.resources.batch_export_title
import com.module.notelycompose.resources.batch_export_txt_files
import com.module.notelycompose.resources.cancel
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExportSelectedItemConfirmationDialog(
    showExportNotesConfirmDialog: Boolean,
    onExport: (exportAudio: Boolean, exportTxt: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    if(showExportNotesConfirmDialog) {
        var exportAudio by remember { mutableStateOf(true) }
        var exportTxt by remember { mutableStateOf(true) }

        val hasSelection = exportAudio || exportTxt

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = stringResource(Res.string.batch_export_options),
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(Res.string.batch_export_select_formats),
                        style = MaterialTheme.typography.body2,
                        color = LocalCustomColors.current.bodyContentColor.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    ExportOption(
                        text = stringResource(Res.string.batch_export_audio_files),
                        checked = exportAudio,
                        onCheckedChange = { exportAudio = it }
                    )

                    ExportOption(
                        text = stringResource(Res.string.batch_export_txt_files),
                        checked = exportTxt,
                        onCheckedChange = { exportTxt = it }
                    )

                    if (!hasSelection) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(Res.string.batch_export_select_at_least_one_option),
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier.padding(top = 12.dp),
                            fontSize = 16.sp
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismiss().also {
                            onExport(exportAudio, exportTxt)
                        }
                    },
                    enabled = hasSelection,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (hasSelection) {
                            MaterialTheme.colors.primary
                        } else {
                            LocalCustomColors.current.shareDialogBackgroundColor
                        },
                        contentColor = if (hasSelection) {
                            MaterialTheme.colors.onPrimary
                        } else {
                            LocalCustomColors.current.bodyContentColor.copy(alpha = 0.4f)
                        },
                        disabledBackgroundColor = LocalCustomColors.current.shareDialogBackgroundColor,
                        disabledContentColor = LocalCustomColors.current.bodyContentColor.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.batch_export_title),
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = LocalCustomColors.current.shareDialogBackgroundColor,
                        contentColor = LocalCustomColors.current.bodyContentColor
                    ),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = stringResource(Res.string.cancel))
                }
            },
            backgroundColor = LocalCustomColors.current.shareDialogBackgroundColor,
            contentColor = LocalCustomColors.current.bodyContentColor
        )
    }
}

@Composable
private fun ExportOption(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(end = 8.dp, top = 4.dp, bottom = 4.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = LocalCustomColors.current.selectAllCheckboxColor
                )
            )
        }

        Text(
            text = text,
            color = LocalCustomColors.current.bodyContentColor,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(start = 2.dp)
        )
    }
}