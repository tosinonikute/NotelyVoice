package com.module.notelycompose.android.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.module.notelycompose.android.presentation.AndroidNoteListViewModel
import com.module.notelycompose.notes.presentation.list.NoteListIntent.OnNoteDeleted
import com.module.notelycompose.notes.presentation.list.NoteListIntent.OnFilterNote
import com.module.notelycompose.notes.presentation.list.NoteListIntent.OnSearchNote
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
            viewmodel.onProcessIntent(OnNoteDeleted(it))
        },
        onFilterTabItemClicked = { filter ->
            viewmodel.onProcessIntent(OnFilterNote(filter))
        },
        onSearchByKeyword = { keyword ->
            viewmodel.onProcessIntent(OnSearchNote(keyword))
        },
        selectedTabTitle = state.selectedTabTitle
    )
}
