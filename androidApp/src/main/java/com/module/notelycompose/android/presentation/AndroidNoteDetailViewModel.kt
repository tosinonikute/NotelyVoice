package com.module.notelycompose.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetLastNote
import com.module.notelycompose.notes.domain.GetNoteById
import com.module.notelycompose.notes.domain.InsertNoteUseCase
import com.module.notelycompose.notes.domain.Note
import com.module.notelycompose.notes.domain.UpdateNoteUseCase
import com.module.notelycompose.notes.presentation.detail.NoteDetailScreenEvent
import com.module.notelycompose.notes.presentation.detail.NoteDetailScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidNoteDetailViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteById,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val deleteNoteById: DeleteNoteById,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val getLastNoteUseCase: GetLastNote
) : ViewModel() {

    private val viewModel by lazy {
        NoteDetailScreenViewModel(
            getNoteByIdUseCase = getNoteByIdUseCase,
            insertNoteUseCase = insertNoteUseCase,
            deleteNoteUseCase = deleteNoteById,
            updateNoteUseCase = updateNoteUseCase,
            getLastNoteUseCase = getLastNoteUseCase,
            coroutineScope = viewModelScope
        )
    }
    val state = viewModel.state

    fun getNoteById(id: String): Note? {
        return viewModel.getNoteById(id)
    }

    fun onEvent(event: NoteDetailScreenEvent) {
        viewModel.onEvent(event)
    }

    fun onCreateOrUpdateEvent(
        title: String,
        content: String,
        isUpdate: Boolean
    ) {
        viewModel.onCreateOrUpdateEvent(title, content, isUpdate)
    }

    // TODO: use NoteDetailScreenUiState to set the state
    fun getNewNoteContentDate(id: String) = viewModel.getNewNoteContentDate(id)
}