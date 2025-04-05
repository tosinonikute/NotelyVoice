package com.module.notelycompose

import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetAllNotesUseCase
import com.module.notelycompose.notes.domain.SearchNotesUseCase
import com.module.notelycompose.notes.presentation.list.NoteListEvent
import com.module.notelycompose.notes.presentation.list.NoteListViewModel
import com.module.notelycompose.notes.presentation.list.mapper.NotesFilterMapper
import com.module.notelycompose.notes.presentation.mapper.NoteUiMapper

class IOSNoteListViewModel (
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val searchNotesUseCase: SearchNotesUseCase,
    private val deleteNoteById: DeleteNoteById,
    private val noteUiMapper: NoteUiMapper,
    private val notesFilterMapper: NotesFilterMapper
) {

    private val viewModel by lazy {
        NoteListViewModel(
            getAllNotesUseCase = getAllNotesUseCase,
            searchNotesUseCase = searchNotesUseCase,
            deleteNoteById = deleteNoteById,
            noteUiMapper = noteUiMapper,
            notesFilterMapper = notesFilterMapper,
            coroutineScope = null
        )
    }
    val state = viewModel.state

    fun onEvent(event: NoteListEvent) {
        viewModel.onEvent(event)
    }
}