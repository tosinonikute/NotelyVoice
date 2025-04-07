package com.module.notelycompose.android.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.module.notelycompose.android.presentation.AndroidNoteListViewModel
import com.module.notelycompose.notes.presentation.list.NoteListEvent
import com.module.notelycompose.notes.ui.list.SharedNoteListScreen

@Composable
fun NoteListScreen(
    viewmodel: AndroidNoteListViewModel,
    onFloatingActionButtonClicked: () -> Unit,
    onNoteClicked: (Long) -> Unit
) {
    val state by viewmodel.state.collectAsState()
    val notes = viewmodel.onGetUiState(state)

    SharedNoteListScreen(
        notes = notes,
        onFloatingActionButtonClicked = {
            onFloatingActionButtonClicked()
        },
        onNoteClicked = {
            onNoteClicked(it)
        },
        onNoteDeleteClicked = {
            viewmodel.onEvent(NoteListEvent.OnNoteDeleted(it))
        },
        onFilterTabItemClicked = { filter ->
            viewmodel.onEvent(NoteListEvent.OnFilterNote(filter))
        },
        onSearchByKeyword = { keyword ->
            viewmodel.onEvent(NoteListEvent.OnSearchNote(keyword))
        }
    )
}
