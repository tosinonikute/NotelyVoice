package com.module.notelycompose.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.android.R
import com.module.notelycompose.notes.domain.Note

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
            .background(Color.LightGray) // Add background to the Row
            .padding(12.dp), // Add padding to the Row
            //.clip(RoundedCornerShape(16.dp)), // Apply rounded corners
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .clip(RoundedCornerShape(6.dp))
                .size(32.dp) // Set the size of the box
                .background(Color.Gray) // Add a light gray background
                // Add padding inside the box
                .fillMaxHeight()
                .padding(4.dp)

        ) {
            Icon(
                painter = icon,
                contentDescription = "Note icon",
                tint = Color.Black // Set icon tint to black
            )
        }
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
