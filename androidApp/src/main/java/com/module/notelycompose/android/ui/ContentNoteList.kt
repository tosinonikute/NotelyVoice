package com.module.notelycompose.android.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.module.notelycompose.notes.domain.model.Note

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentNoteList(
    noteList: List<Note>,
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
//            ContentNoteItem(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 12.dp)
//                    .animateItemPlacement()
//                    .clip(RoundedCornerShape(16.dp)),
//                note = note,
//                icon = iconList[index],
//                onNoteClick = {
//                    onNoteClicked(note.id)
//                },
//                onDeleteClick = {
//                    onNoteDeleteClicked(note.id)
//                }
//            )
            ContentNoteCard()
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
