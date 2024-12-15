package com.module.notelycompose

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.module.notelycompose.notes.domain.Note
import com.module.notelycompose.notes.presentation.detail.userinterface.NoteDetailScreen
import com.module.notelycompose.notes.presentation.list.NoteListEvent
import com.module.notelycompose.notes.presentation.list.userinterface.SharedNoteListScreen
import com.module.notelycompose.notes.presentation.theme.MyApplicationTheme
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(DelicateCoroutinesApi::class)
fun NoteListController(
    onFloatingActionButtonClicked: () -> Unit,
    onNoteClicked: (Int) -> Unit,
) = ComposeUIViewController {
    MyApplicationTheme {
        val appModule = remember { AppModule() }
        val viewmodel = remember {
            IOSNoteListViewModel(
                getAllNotesUseCase = appModule.getAllNotesUseCase,
                insertNoteUseCase = appModule.insertNote,
                deleteNoteById = appModule.deleteNoteById
            )
        }
        val state = viewmodel.state.collectAsState()
        SharedNoteListScreen(
            state.value, onFloatingActionButtonClicked, onNoteClicked
        ) { id ->
            viewmodel.onEvent(NoteListEvent.OnNoteDeleted(id))
        }
    }
}

fun NoteDetailController(
    noteId: String? = null,
    onSaveClicked: () -> Unit
) = ComposeUIViewController {
    MyApplicationTheme {
        val appModule = AppModule()
        val viewModel = remember {
            IOSNoteDetailViewModel(
                getNoteByIdUseCase = appModule.getNoteById,
                deleteNoteUseCase = appModule.deleteNoteById,
                insertNoteUseCase = appModule.insertNote,
                updateNoteUseCase = appModule.updateNote,
                getLastNoteUseCase = appModule.getLastNoteUseCase
            )
        }

        val note: Note? = remember {
            if (noteId != null) viewModel.getNoteById(noteId) else null
        }
        val newNoteDateString = noteId?.let { viewModel.getNewNoteContentDate(noteId) } ?: ""

        NoteDetailScreen(
            title = note?.title,
            content = note?.content,
            newNoteDateString = newNoteDateString,
            onSaveClick = { title, content, isUpdate ->
                viewModel.onCreateOrUpdateEvent(
                    title = title,
                    content = content,
                    isUpdate = isUpdate
                )
                onSaveClicked()
            },
            onNavigateBack = {},
        )
    }
}
