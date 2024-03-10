package com.module.notelycompose

import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetNoteById
import com.module.notelycompose.notes.domain.InsertNoteUseCase
import com.module.notelycompose.notes.presentation.detail.NoteDetailScreenEvent
import com.module.notelycompose.notes.presentation.detail.NoteDetailScreenViewModel

class IOSNoteDetailViewModel(
    private val getNoteByIdUseCase: GetNoteById,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteById
) {

    private val viewModel by lazy {
        NoteDetailScreenViewModel(
            getNoteByIdUseCase = getNoteByIdUseCase,
            insertNoteUseCase = insertNoteUseCase,
            deleteNoteUseCase = deleteNoteUseCase
        )
    }
    val state = viewModel.state

    fun getNoteById(id: String) = viewModel.getNoteById(id)

    fun onEvent(event: NoteDetailScreenEvent) {
        viewModel.onEvent(event)
    }
}