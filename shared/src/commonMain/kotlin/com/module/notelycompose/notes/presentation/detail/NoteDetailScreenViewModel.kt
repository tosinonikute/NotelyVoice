package com.module.notelycompose.notes.presentation.detail

import com.module.notelycompose.core.toCommonStateFlow
import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetNoteById
import com.module.notelycompose.notes.domain.InsertNoteUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteDetailScreenViewModel(
    private val getNoteByIdUseCase: GetNoteById,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteById,
    coroutineScope: CoroutineScope? = null
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(NoteDetailScreenUiState())

    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        NoteDetailScreenUiState()
    ).toCommonStateFlow()

    fun getNoteById(id: String) = getNoteByIdUseCase.execute(id)

    private fun insertNote(
        title: String,
        content: String
    ) {
        viewModelScope.launch {
            insertNoteUseCase.execute(
                title, content
            )
        }
    }

    fun onEvent(event: NoteDetailScreenEvent) {
        when (event) {
            is NoteDetailScreenEvent.NoteSaved -> {
                insertNote(
                    title = event.title,
                    content = event.content
                )
            }
            is NoteDetailScreenEvent.GetNote -> {
                getNoteById(event.id)
            }
            is NoteDetailScreenEvent.DeleteNote -> {
                viewModelScope.launch {
                    deleteNoteUseCase.execute(event.id.toInt())
                }
            }
        }
    }
}