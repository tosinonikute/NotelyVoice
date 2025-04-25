package com.module.notelycompose.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetAllNotesUseCase
import com.module.notelycompose.notes.domain.SearchNotesUseCase
import com.module.notelycompose.notes.presentation.list.NoteListIntent
import com.module.notelycompose.notes.presentation.list.NoteListPresentationState
import com.module.notelycompose.notes.presentation.list.NoteListViewModel
import com.module.notelycompose.notes.presentation.list.mapper.NotesFilterMapper
import com.module.notelycompose.notes.presentation.mapper.NotePresentationMapper
import com.module.notelycompose.notes.ui.list.model.NoteUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidNoteListViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteById: DeleteNoteById,
    private val notePresentationMapper: NotePresentationMapper,
    private val notesFilterMapper: NotesFilterMapper
) : ViewModel() {

    private val viewModel by lazy {
        NoteListViewModel(
            selectedTabTitle = "",
            getAllNotesUseCase = getAllNotesUseCase,
            deleteNoteById = deleteNoteById,
            notePresentationMapper = notePresentationMapper,
            notesFilterMapper = notesFilterMapper,
            coroutineScope = viewModelScope
        )
    }
    val state = viewModel.state

    fun onProcessIntent(intent: NoteListIntent) {
        viewModel.onProcessIntent(intent)
    }

    fun onGetUiState(presentationState: NoteListPresentationState): List<NoteUiModel> {
        return viewModel.onGetUiState(presentationState)
    }
}
