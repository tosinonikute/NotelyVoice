package com.module.notelycompose.notes.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Alignment.Companion.TopCenter
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
import notelycompose.composeapp.generated.resources.Res
import notelycompose.composeapp.generated.resources.note_item_delete
import notelycompose.composeapp.generated.resources.note_item_edit
import notelycompose.composeapp.generated.resources.words
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

private const val ZERO_WORDS = 0

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteItem(
    modifier: Modifier,
    note: NoteUiModel,
    onNoteClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    Card(
            modifier
                .fillMaxWidth()
                .clickable {
                    onNoteClick(note.id)
                },
            elevation = 4.dp,
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color(0xFFD18B60)
        ) {
                Column(modifier = Modifier.padding(10.dp).fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = note.title,
                            color = LocalCustomColors.current.noteTextColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        IconButton(modifier = Modifier.size(20.dp, 20.dp), onClick = {
                            onDeleteClick(note.id)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                tint = LocalCustomColors.current.noteIconColor,
                                contentDescription = stringResource(Res.string.note_item_delete)
                            )
                        }
                    }
                    Text(
                        text = note.content,
                        color = LocalCustomColors.current.noteTextColor,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 14.dp, bottom = 8.dp),
                        maxLines = 10,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = CenterVertically
                    ) {
                        FlowRow(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(4.dp)

                                    )
                                }
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = note.createdAt,
                            color = LocalCustomColors.current.noteTextColor,
                            fontSize = 12.sp,
                        )
                        IconButton(modifier = Modifier.size(20.dp, 20.dp), onClick = { onNoteClick(note.id) }) {
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
