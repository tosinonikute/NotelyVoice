package com.module.notelycompose

import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetAllNotesUseCase
import com.module.notelycompose.notes.domain.SearchNotesUseCase
import com.module.notelycompose.notes.presentation.list.NoteListEvent
import com.module.notelycompose.notes.presentation.list.NoteListPresentationState
import com.module.notelycompose.notes.presentation.list.NoteListViewModel
import com.module.notelycompose.notes.presentation.list.mapper.NotesFilterMapper
import com.module.notelycompose.notes.presentation.mapper.NotePresentationMapper
import com.module.notelycompose.notes.ui.list.model.NoteUiModel

class IOSNoteListViewModel (
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val searchNotesUseCase: SearchNotesUseCase,
    private val deleteNoteById: DeleteNoteById,
    private val notePresentationMapper: NotePresentationMapper,
    private val notesFilterMapper: NotesFilterMapper
) {

    private val viewModel by lazy {
        NoteListViewModel(
            getAllNotesUseCase = getAllNotesUseCase,
            searchNotesUseCase = searchNotesUseCase,
            deleteNoteById = deleteNoteById,
            notePresentationMapper = notePresentationMapper,
            notesFilterMapper = notesFilterMapper,
            coroutineScope = null
        )
    }
    val state = viewModel.state

    fun onEvent(event: NoteListEvent) {
        viewModel.onEvent(event)
    }

    fun onGetUiState(presentationState: NoteListPresentationState): List<NoteUiModel> {
        return viewModel.onGetUiState(presentationState)
    }
}