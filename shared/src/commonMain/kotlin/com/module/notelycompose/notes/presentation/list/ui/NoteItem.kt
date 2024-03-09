package com.module.notelycompose.notes.presentation.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.notes.domain.Note

@Composable
fun NoteItem(
    modifier: Modifier,
    note: Note,
    onNoteClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(note.colorHex))
            .clickable { onNoteClick(note.id) }
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = note.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete note",
                modifier = Modifier
                    .clickable(interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false)) {
                        onDeleteClick(note.id)
                    }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = note.content, fontWeight = FontWeight.Light)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Note date",
                tint = Color.DarkGray
            )
            Text(
                text ="${note.createdAt.dayOfMonth} ${note.createdAt.month.name} ${note.createdAt.year}",
                color = Color.DarkGray,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}