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
    onNoteDeleteClicked: (Long) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(items = noteList) { index, note ->
            NoteItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .animateItemPlacement(),
                note = note,
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
