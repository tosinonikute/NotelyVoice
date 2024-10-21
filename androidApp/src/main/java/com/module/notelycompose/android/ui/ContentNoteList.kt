package com.module.notelycompose.android.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.module.notelycompose.android.R
import com.module.notelycompose.notes.domain.Note
import com.module.notelycompose.notes.presentation.list.ui.NoteItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentNoteList(
    noteList: List<Note>,
    iconList: List<Painter>,
    onNoteClicked: (Int) -> Unit,
    onNoteDeleteClicked: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(
            start = 20.dp,
            end = 20.dp,
        )
    ) {
        itemsIndexed(items = noteList) { index, note ->
            ContentNoteItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .animateItemPlacement()
                    .clip(RoundedCornerShape(16.dp)),
                note = note,
                icon = iconList[index],
                onNoteClick = {
                    onNoteClicked(note.id)
                },
                onDeleteClick = {
                    onNoteDeleteClicked(note.id)
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
