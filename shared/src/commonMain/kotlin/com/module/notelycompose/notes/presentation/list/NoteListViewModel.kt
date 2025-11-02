package com.module.notelycompose.notes.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import audio.utils.deleteFile
import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetAllNotesUseCase
import com.module.notelycompose.notes.domain.model.NoteDomainModel
import com.module.notelycompose.notes.domain.model.NotesFilterDomainModel
import com.module.notelycompose.notes.presentation.helpers.getFirstNonEmptyLineAfterFirst
import com.module.notelycompose.notes.presentation.helpers.returnFirstLine
import com.module.notelycompose.notes.presentation.helpers.truncateWithEllipsis
import com.module.notelycompose.notes.presentation.list.mapper.NotesFilterMapper
import com.module.notelycompose.notes.presentation.list.model.NotePresentationModel
import com.module.notelycompose.notes.presentation.mapper.NotePresentationMapper
import com.module.notelycompose.notes.ui.list.model.NoteUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val DEFAULT_TITLE = "New Note"
const val DEFAULT_CONTENT = "No additional text"
const val CONTENT_LENGTH = 36
private const val SEARCH_DEBOUNCE = 300L

class NoteListViewModel(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteById: DeleteNoteById,
    private val notePresentationMapper: NotePresentationMapper,
    private val notesFilterMapper: NotesFilterMapper,
) :ViewModel(){
    private val _state = MutableStateFlow(NoteListPresentationState())
    val state: StateFlow<NoteListPresentationState> = _state

    // Search query flow
    private val searchQuery = MutableStateFlow("")

    init {
        setupNoteFlow()
        setupSearch()
    }

    private fun setupSearch() {
        // Combine notes flow with filter and search

        searchQuery.debounce(SEARCH_DEBOUNCE)
            .onEach { query ->
                _state.update { currentState ->
                    val filtered = applyFilters(_state.value.originalNotes, _state.value.selectedTabIndex, query)
                    val allNotesSizeStr = if (filtered.isEmpty()) "" else "(${filtered.size})"
                    currentState.copy(
                        filteredNotes = filtered,
                        showEmptyContent = filtered.isEmpty(),
                        allNotesSizeStr = allNotesSizeStr
                    )
                }
            }.launchIn(viewModelScope)
    }

    fun onProcessIntent(intent: NoteListIntent) {
        when (intent) {
            is NoteListIntent.OnNoteDeleted -> handleNoteDeletion(intent.note)
            is NoteListIntent.OnFilterNote -> setSelectedTab(intent.filter)
            is NoteListIntent.OnSearchNote -> searchQuery.value = intent.keyword
        }
    }

    private fun setupNoteFlow() {
        // Combine notes flow with filter and search
        combine(
            getAllNotesUseCase.execute(),
            _state.map { it.selectedTabIndex }.distinctUntilChanged(),
            ) { notes, filter ->
            Pair(notes, filter)
        }.onEach { (notes, filter) ->
            handleNotesUpdate(notes, filter, "")
        }.launchIn(viewModelScope)
    }

    private fun domainToPresentationModel(note: NoteDomainModel): NotePresentationModel {
        val retrievedNote = notePresentationMapper.mapToPresentationModel(note)
        return retrievedNote.copy(
            title = note.title.trim().takeIf { it.isNotEmpty() }
                ?.returnFirstLine()
                ?.truncateWithEllipsis()
                ?: DEFAULT_TITLE,
            content = note.content.trim().takeIf { it.isNotEmpty() }
                ?.getFirstNonEmptyLineAfterFirst()
                ?.truncateWithEllipsis(CONTENT_LENGTH)
                ?: DEFAULT_CONTENT
        )
    }

    private fun applyFilters(
        notes: List<NotePresentationModel>,
        selectedTabIndex: Int,
        query: String
    ): List<NotePresentationModel> {
        val domainFilter = notesFilterMapper.mapToDomainModel(
            notesFilterMapper.mapStringToPresentationModel(selectedTabIndex)
        )
        if (query.isBlank() && (domainFilter == NotesFilterDomainModel.ALL || domainFilter == NotesFilterDomainModel.RECENT)) {
            return notes
        }
        return notes.filter { note ->
            matchesFilter(note, domainFilter) && matchesSearch(
                note,
                query
            )
        }
    }

    private fun handleNotesUpdate(
        notes: List<NoteDomainModel>,
        selectedTabIndex: Int,
        query: String
    ) {

        val presentationNotes = notes.map { domainToPresentationModel(it) }
        val allNotesSizeStr = if (presentationNotes.isEmpty()) "" else "(${presentationNotes.size})"

        _state.update { currentState ->
            currentState.copy(
                originalNotes = presentationNotes,
                filteredNotes = applyFilters(presentationNotes, selectedTabIndex, query),
                showEmptyContent = presentationNotes.isEmpty(),
                allNotesSizeStr = allNotesSizeStr
            )
        }
    }

    private fun matchesFilter(note: NotePresentationModel, filter: NotesFilterDomainModel): Boolean {
        return when (filter) {
            NotesFilterDomainModel.VOICES -> isVoiceNote(note)
            NotesFilterDomainModel.STARRED -> isStarred(note)
            NotesFilterDomainModel.ALL, NotesFilterDomainModel.RECENT -> true
        }
    }

    private fun matchesSearch(note: NotePresentationModel, query: String): Boolean {
        if (query.isBlank()) return true
        return note.title.contains(query, ignoreCase = true) ||
                note.content.contains(query, ignoreCase = true)
    }

    private fun handleNoteDeletion(note: NoteUiModel) {
        viewModelScope.launch {
            deleteFile(note.recordingPath)
            deleteNoteById.execute(note.id)
            // No need to manually refresh - flow will handle it
        }
    }

    fun onGetUiState(presentationState: NoteListPresentationState): List<NoteUiModel> {
        return presentationState.filteredNotes.map { notePresentationMapper.mapToUiModel(it) }
    }

    private fun setSelectedTab(tabIndex: Int) {
        _state.value = _state.value.copy(selectedTabIndex = tabIndex)
    }

    private fun isVoiceNote(note: NotePresentationModel): Boolean {
        return note.recordingPath.isNotEmpty()
    }

    private fun isStarred(note: NotePresentationModel): Boolean {
        return note.isStarred
    }
}
