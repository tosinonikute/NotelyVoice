package com.module.notelycompose.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetAllNotesUseCase
import com.module.notelycompose.notes.domain.InsertNoteUseCase
import com.module.notelycompose.notes.presentation.list.NoteListEvent
import com.module.notelycompose.notes.presentation.list.NoteListViewModel
import com.module.notelycompose.notes.presentation.mapper.NoteUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidNoteListViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteById: DeleteNoteById,
    private val noteUiMapper: NoteUiMapper
) : ViewModel() {

    private val viewModel by lazy {
        NoteListViewModel(
            getAllNotesUseCase = getAllNotesUseCase,
            deleteNoteById = deleteNoteById,
            noteUiMapper = noteUiMapper,
            coroutineScope = viewModelScope
        )
    }
    val state = viewModel.state

    fun onEvent(event: NoteListEvent) {
        viewModel.onEvent(event)
    }
}
