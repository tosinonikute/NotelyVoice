package com.module.notelycompose.notes.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.module.notelycompose.notes.ui.list.model.NoteUiModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteList(
    noteList: List<NoteUiModel>,
    onNoteClicked: (Long) -> Unit,
    onNoteDeleteClicked: (NoteUiModel) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = 200.dp),
        modifier = Modifier.padding(top = 16.dp, start = 20.dp, end = 20.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(items = noteList) { index, note ->
            NoteItem(
                note = note,
                onNoteClick = {
                    onNoteClicked(note.id)
                },
                onDeleteClick = {
                    onNoteDeleteClicked(note)
                }
            )
        }

    }
}
