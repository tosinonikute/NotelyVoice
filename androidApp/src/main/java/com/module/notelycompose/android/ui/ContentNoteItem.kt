package com.module.notelycompose.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.module.notelycompose.android.theme.LocalCustomColors
import com.module.notelycompose.notes.domain.model.Note

@Composable
fun ContentNoteItem(
    modifier: Modifier,
    note: Note,
    icon: Painter,
    onNoteClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            //.padding(vertical = 8.dp)
            .background(LocalCustomColors.current.noteListBackgroundColor) // Add background to the Row
            .padding(12.dp), // Add padding to the Row
            //.clip(RoundedCornerShape(16.dp)), // Apply rounded corners
        verticalAlignment = Alignment.Top
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = note.title.take(20).let { if (it.length < note.title.length) "$it..." else it },
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = note.content,
                style = MaterialTheme.typography.body1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column( // Wrap time text in a column for top alignment
            modifier = Modifier.align(Alignment.Top)
        ) {
            Text(
                text = "1:09AM",
                style = MaterialTheme.typography.caption,
                color = Color.Gray
            )
        }
    }
}
