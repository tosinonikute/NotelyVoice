package com.module.notelycompose.notes.presentation.list

import com.module.notelycompose.core.toCommonStateFlow
import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetAllNotesUseCase
import com.module.notelycompose.notes.domain.InsertNoteUseCase
import com.module.notelycompose.notes.domain.UpdateNoteUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteListViewModel(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteById: DeleteNoteById,
    private val insertNoteUseCase: InsertNoteUseCase,
    coroutineScope: CoroutineScope? = null
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(NoteListUiState())
    val state = combine(
        _state,
        getAllNotesUseCase.execute()
    ) { state, notes ->
        state.copy(
            notes = notes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteListUiState())
        .toCommonStateFlow()

    fun onEvent(event: NoteListEvent) {
        when (event) {
            is NoteListEvent.OnNoteDeleted -> {
                viewModelScope.launch {
                    deleteNoteById.execute(event.id)
                }
            }
            is NoteListEvent.InsertNote -> {
                viewModelScope.launch {
                    insertNoteUseCase.execute(event.title, event.content)
                }
            }
        }
    }
}