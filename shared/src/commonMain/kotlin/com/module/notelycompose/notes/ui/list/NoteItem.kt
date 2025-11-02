package com.module.notelycompose.notes.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.notes.ui.detail.DeleteConfirmationDialog
import com.module.notelycompose.notes.ui.list.model.NoteUiModel
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.resources.vectors.IcArrowUpRight
import com.module.notelycompose.resources.vectors.Images
import com.module.notelycompose.resources.Res
import com.module.notelycompose.resources.note_item_delete
import com.module.notelycompose.resources.note_item_edit
import com.module.notelycompose.resources.words
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

private const val ZERO_WORDS = 0

@Composable
fun NoteItem(
    note: NoteUiModel,
    onNoteClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
    isChecked: Boolean,
    onCheckedChange: (Long, Boolean) -> Unit,
    isSelectAllAction: Boolean,
    onNoteLongPress: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    DeleteConfirmationDialog(
        showDialog = showDeleteDialog,
        onDismiss = { showDeleteDialog = false },
        onConfirm = { onDeleteClick(note.id) }
    )

    Row {

        if(isSelectAllAction) {
            Column {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { onCheckedChange(note.id, it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = LocalCustomColors.current.selectAllCheckboxColor
                    )
                )
            }
        }

        Column {

            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                        .combinedClickable(
                            onClick = { onNoteClick(note.id) },
                            onLongClick = { onNoteLongPress() }
                        ),
                elevation = 2.dp,
                shape = RoundedCornerShape(28.dp),
                backgroundColor = Color(0xFFD18B60)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = note.createdAt,
                            color = LocalCustomColors.current.noteTextColor,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                showDeleteDialog = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                tint = LocalCustomColors.current.noteIconColor,
                                contentDescription = stringResource(Res.string.note_item_delete)
                            )
                        }
                    }
                    Text(
                        text = note.title,
                        color = LocalCustomColors.current.noteTextColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = note.content,
                        color = LocalCustomColors.current.noteTextColor,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FlowRow (
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalArrangement = Arrangement.SpaceBetween){
                            NoteType(
                                isStarred = note.isStarred,
                                isVoice = note.isVoice
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            if (note.words > ZERO_WORDS) {
                                Card(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    backgroundColor = Color(0xFFD18B60).copy(alpha = 0.5f)
                                ) {
                                    Text(
                                        text = pluralStringResource(Res.plurals.words, note.words, note.words),
                                        color = LocalCustomColors.current.noteTextColor,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        Card(
                            modifier = Modifier.padding(bottom = 4.dp),
                            shape = RoundedCornerShape(32.dp),
                            backgroundColor = Color(0xFFD18B60)
                        ) {
                            IconButton(onClick = { onNoteClick(note.id) }) {
                                Icon(
                                    imageVector = Images.Icons.IcArrowUpRight,
                                    tint = LocalCustomColors.current.noteIconColor,
                                    contentDescription = stringResource(Res.string.note_item_edit)
                                )
                            }
                        }
                    }
                }
            }

            // end
        }
    }


}