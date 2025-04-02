package com.module.notelycompose.notes.presentation.list

import com.module.notelycompose.core.toCommonStateFlow
import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetAllNotesUseCase
import com.module.notelycompose.notes.domain.model.NotesFilter
import com.module.notelycompose.notes.presentation.mapper.NoteUiMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

const val DEFAULT_TITLE = "New Note"
const val DEFAULT_CONTENT = "No additional text"
const val STATE_SUBSCRIPTION_TIMEOUT = 5000L

class NoteListViewModel(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteById: DeleteNoteById,
    private val noteUiMapper: NoteUiMapper,
    coroutineScope: CoroutineScope? = null
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(NoteListUiState())
    val state = createCombinedState(NotesFilter.ALL)

    private fun createCombinedState(filter: NotesFilter) = combine(
        _state,
        getAllNotesUseCase.execute(filter)
    ) { state, notes ->
        state.copy(
            notes = notes.map { note ->
                val retrievedNote = noteUiMapper.mapToUiModel(note)
                retrievedNote.copy(
                    title = if (note.title.trim().isEmpty()) DEFAULT_TITLE else note.title,
                    content = if (note.content.trim().isEmpty()) DEFAULT_CONTENT else note.content
                )
            }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(STATE_SUBSCRIPTION_TIMEOUT), NoteListUiState())
        .toCommonStateFlow()

    fun onEvent(event: NoteListEvent) {
        when (event) {
            is NoteListEvent.OnNoteDeleted -> {
                viewModelScope.launch {
                    deleteNoteById.execute(event.id)
                }
            }
            is NoteListEvent.OnStarredNote -> {
                createCombinedState(NotesFilter.STARRED)
            }
            is NoteListEvent.OnVoiceNote -> {
                createCombinedState(NotesFilter.VOICES)
            }
        }
    }
}
