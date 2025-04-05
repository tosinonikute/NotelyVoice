package com.module.notelycompose.notes.presentation.list

import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetAllNotesUseCase
import com.module.notelycompose.notes.domain.model.NotesFilterDomainModel
import com.module.notelycompose.notes.domain.model.NotesFilterDomainModel.ALL
import com.module.notelycompose.notes.presentation.helpers.getFirstNonEmptyLineAfterFirst
import com.module.notelycompose.notes.presentation.helpers.replaceNewLinesWithSpaces
import com.module.notelycompose.notes.presentation.helpers.truncateWithEllipsis
import com.module.notelycompose.notes.presentation.list.mapper.NotesFilterMapper
import com.module.notelycompose.notes.presentation.mapper.NoteUiMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

const val DEFAULT_TITLE = "New Note"
const val DEFAULT_CONTENT = "No additional text"
const val CONTENT_LENGTH = 36

class NoteListViewModel(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteById: DeleteNoteById,
    private val noteUiMapper: NoteUiMapper,
    private val notesFilterMapper: NotesFilterMapper,
    coroutineScope: CoroutineScope? = null
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(NoteListUiState())
    val state: StateFlow<NoteListUiState> = _state

    init {
        createCombinedState(ALL)
    }

    private fun createCombinedState(
        filter: NotesFilterDomainModel
    ) {
        viewModelScope.launch {
            getAllNotesUseCase.execute(filter).collect { notes ->
                _state.value = _state.value.copy(
                    notes = notes.map { note ->
                        val retrievedNote = noteUiMapper.mapToUiModel(note)
                        retrievedNote.copy(
                            title = if (note.title.trim().isEmpty()){
                                DEFAULT_TITLE
                            } else {
                                note.title
                                    .replaceNewLinesWithSpaces()
                                    .truncateWithEllipsis()
                            },
                            content = if (note.content.trim().isEmpty()){
                                DEFAULT_CONTENT
                            } else {
                                note.content
                                    .getFirstNonEmptyLineAfterFirst()
                                    .truncateWithEllipsis(CONTENT_LENGTH)
                            }
                        )
                    }
                )
            }
        }
    }

    fun onEvent(event: NoteListEvent) {
        when (event) {
            is NoteListEvent.OnNoteDeleted -> {
                viewModelScope.launch {
                    deleteNoteById.execute(event.id)
                    createCombinedState(ALL)
                }
            }
            is NoteListEvent.OnFilterNote -> {
                val presentationFilter = notesFilterMapper.mapStringToPresentationModel(event.filter)
                val domainFilter = notesFilterMapper.mapToDomainModel(presentationFilter)
                createCombinedState(domainFilter)
            }
        }
    }
}
