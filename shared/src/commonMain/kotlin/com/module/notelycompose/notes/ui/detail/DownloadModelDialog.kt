package com.module.notelycompose.notes.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.module.notelycompose.modelDownloader.HINDI_MODEL
import org.jetbrains.compose.resources.stringResource
import com.module.notelycompose.resources.Res
import com.module.notelycompose.resources.download_required
import com.module.notelycompose.resources.download_required_for_hindi
import com.module.notelycompose.resources.for_accurate_transcription
import com.module.notelycompose.resources.take_few_minutes
import com.module.notelycompose.resources.download
import com.module.notelycompose.resources.cancel
import com.module.notelycompose.modelDownloader.TranscriptionModel
import com.module.notelycompose.resources.file_size_approx
import com.module.notelycompose.resources.file_model_english
import com.module.notelycompose.resources.file_model_hindi

@Composable
fun DownloadModelDialog(
    onDownload: () -> Unit,
    onCancel: () -> Unit,
    transcriptionModel: TranscriptionModel,
    modifier: Modifier = Modifier
) {
    val fileInfo: String = if(transcriptionModel.getModelDownloadType() == HINDI_MODEL) {
        stringResource(Res.string.file_model_hindi)
    } else {
        stringResource(Res.string.file_model_english)
    }

    val downloadRequired: String = if(transcriptionModel.getModelDownloadType() == HINDI_MODEL) {
        stringResource(Res.string.download_required_for_hindi)
    } else {
        stringResource(Res.string.download_required)
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onCancel,
        title = {
            Text(text = downloadRequired)
        },
        text = {
            Column {
                Text(stringResource(Res.string.for_accurate_transcription))
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(Res.string.take_few_minutes))
                Spacer(modifier = Modifier.height(8.dp))
                Text(fileInfo)
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(Res.string.file_size_approx, transcriptionModel.getModelDownloadSize()))
            }
        },
        confirmButton = {
            Button(
                onClick = onDownload,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White
                )
            ) {
                Text(stringResource(Res.string.download))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text(stringResource(Res.string.cancel))
            }
        }
    )
}