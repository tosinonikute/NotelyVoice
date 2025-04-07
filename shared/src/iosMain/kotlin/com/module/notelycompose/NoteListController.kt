package com.module.notelycompose

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.module.notelycompose.notes.presentation.list.NoteListEvent
import com.module.notelycompose.notes.ui.list.SharedNoteListScreen
import com.module.notelycompose.notes.ui.theme.MyApplicationTheme

fun NoteListController(
    onFloatingActionButtonClicked: () -> Unit,
    onNoteClicked: (Long) -> Unit,
) = ComposeUIViewController {
    MyApplicationTheme {
        val appModule = remember { AppModule() }
        val viewmodel = remember {
            IOSNoteListViewModel(
                getAllNotesUseCase = appModule.getAllNotesUseCase,
                searchNotesUseCase = appModule.searchNotesUseCase,
                deleteNoteById = appModule.deleteNoteById,
                notePresentationMapper = appModule.notePresentationMapper,
                notesFilterMapper = appModule.notesFilterMapper
            )
        }
        val state = viewmodel.state.collectAsState()
        val notes = viewmodel.onGetUiState(state.value)
        SharedNoteListScreen(
            notes = notes,
            onFloatingActionButtonClicked = onFloatingActionButtonClicked,
            onNoteClicked = onNoteClicked,
            onNoteDeleteClicked = { id ->
                viewmodel.onEvent(NoteListEvent.OnNoteDeleted(id))
            },
            onFilterTabItemClicked = { filter ->
                viewmodel.onEvent(NoteListEvent.OnFilterNote(filter))
            },
            onSearchByKeyword = { keyword ->
                viewmodel.onEvent(NoteListEvent.OnSearchNote(keyword))
            }
        )
    }
}
