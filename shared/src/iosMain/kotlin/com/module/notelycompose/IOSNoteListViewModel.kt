package com.module.notelycompose

import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetAllNotesUseCase
import com.module.notelycompose.notes.domain.InsertNoteUseCase
import com.module.notelycompose.notes.presentation.list.NoteListEvent
import com.module.notelycompose.notes.presentation.list.NoteListViewModel

class IOSNoteListViewModel (
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteById: DeleteNoteById,
    private val insertNoteUseCase: InsertNoteUseCase
) {

    private val viewModel by lazy {
        NoteListViewModel(
            getAllNotesUseCase = getAllNotesUseCase,
            deleteNoteById = deleteNoteById,
            insertNoteUseCase = insertNoteUseCase,
            coroutineScope = null
        )
    }
    val state = viewModel.state

    fun onEvent(event: NoteListEvent) {
        viewModel.onEvent(event)
    }
}