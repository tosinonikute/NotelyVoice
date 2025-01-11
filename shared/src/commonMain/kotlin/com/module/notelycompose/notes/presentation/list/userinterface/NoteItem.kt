package com.module.notelycompose.notes.presentation.list.userinterface

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
import com.module.notelycompose.notes.domain.Note
import com.module.notelycompose.notes.presentation.theme.LocalCustomColors
import com.module.notelycompose.resources.vectors.IcArrowUpRight
import com.module.notelycompose.resources.vectors.Images

@Composable
fun NoteItem(
    modifier: Modifier,
    note: Note,
    onNoteClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
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
                        text = "${note.createdAt.dayOfMonth} ${note.createdAt.month.toString()
                            .lowercase()
                            .replaceFirstChar { 
                                if (it.isLowerCase()) it.titlecase() else it.toString() 
                            } } at ${note.createdAt.hour}:${note.createdAt.minute}",
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
                            contentDescription = "Delete note"
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
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            backgroundColor = Color(0xFFD18B60).copy(alpha = 0.5f)
                        ) {
                            Text(
                                text = "2 notes",
                                color = LocalCustomColors.current.noteTextColor,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            backgroundColor = Color(0xFFD18B60).copy(alpha = 0.5f)
                        ) {
                            Text(
                                text = "250 words",
                                color = LocalCustomColors.current.noteTextColor,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
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
                                contentDescription = "Edit"
                            )
                        }
                    }
                }
            }
        }
    }
}
