package com.module.notelycompose.android.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.android.R
import com.module.notelycompose.android.theme.LocalCustomColors

@Composable
fun ContentNoteCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp,
            shape = RoundedCornerShape(28.dp),
            backgroundColor = Color(0xFFD18B60) // Salmon color
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
//                Text(
//                    text = "02/05",
//                    fontSize = 12.sp,
//                    modifier = Modifier.align(Alignment.Start)
//                )
//                IconButton(onClick = { /* TODO: Edit action */ },
//                    modifier = Modifier.align(Alignment.End))
//                {
//                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
//                }

                Row(
                    modifier = Modifier.fillMaxWidth(), // Makes the Row take full width
                    horizontalArrangement = Arrangement.SpaceBetween, // Aligns items to start and end
                    verticalAlignment = Alignment.CenterVertically // Aligns items vertically in the center
                ) {
                    Text(
                        text = "02/05",
                        color = LocalCustomColors.current.noteTextColor,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Optional; use if necessary for spacing
                    IconButton(
                        onClick = { /* TODO: Close action */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = LocalCustomColors.current.noteIconColor,
                            contentDescription = "Edit"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "technical drawing fundamentals",
                    color = LocalCustomColors.current.noteTextColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "class notes",
                    color = LocalCustomColors.current.noteTextColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
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
                        backgroundColor = Color(0xFFFA8767)
                    ) {
                        IconButton(onClick = { /* TODO: Edit action */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_up_right),
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