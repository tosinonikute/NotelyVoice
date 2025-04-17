package com.module.notelycompose.notes.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.notes.ui.list.model.NoteUiModel
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.resources.vectors.IcArrowUpRight
import com.module.notelycompose.resources.vectors.Images
import notelycompose.shared.generated.resources.Res
import notelycompose.shared.generated.resources.note_item_delete
import notelycompose.shared.generated.resources.note_item_edit
import notelycompose.shared.generated.resources.words
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

private const val ZERO_WORDS = 0

@Composable
fun NoteItem(
    modifier: Modifier,
    note: NoteUiModel,
    onNoteClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                onNoteClick(note.id)
            }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp,
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
                            onDeleteClick(note.id)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = LocalCustomColors.current.noteIconColor,
                            contentDescription = stringResource(Res.string.note_item_delete)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = note.title,
                    color = LocalCustomColors.current.noteTextColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = note.content,
                    color = LocalCustomColors.current.noteTextColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        NoteType(
                            isStarred = note.isStarred,
                            isVoice = note.isVoice
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        if (note.words > ZERO_WORDS) {
                            Card(
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
                        modifier = Modifier.padding(bottom = 8.dp),
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
    }
}
