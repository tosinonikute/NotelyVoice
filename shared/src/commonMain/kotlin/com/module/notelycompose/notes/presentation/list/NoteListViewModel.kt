package com.module.notelycompose.notes.presentation.list

import com.module.notelycompose.audio.ui.expect.deleteFile
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    private val selectedTabTitle:String,
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteById: DeleteNoteById,
    private val notePresentationMapper: NotePresentationMapper,
    private val notesFilterMapper: NotesFilterMapper,
    coroutineScope: CoroutineScope? = null
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(NoteListPresentationState(selectedTabTitle = selectedTabTitle))
    val state: StateFlow<NoteListPresentationState> = _state

    // Search query flow
    private val searchQuery = MutableStateFlow("")

    init {
        setupNoteFlow()
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
            _state.map { it.selectedTabTitle }.distinctUntilChanged(),
            searchQuery.debounce(SEARCH_DEBOUNCE)
        ) { notes, filter, query ->
            Triple(notes, filter, query)
        }.onEach { (notes, filter, query) ->
            handleNotesUpdate(notes, filter, query)
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
        filter: String,
        query: String
    ): List<NotePresentationModel> {
        val domainFilter = notesFilterMapper.mapToDomainModel(
            notesFilterMapper.mapStringToPresentationModel(filter)
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
        filter: String,
        query: String
    ) {

        val presentationNotes = notes.map { domainToPresentationModel(it) }

        _state.update { currentState ->
            currentState.copy(
                originalNotes = presentationNotes,
                filteredNotes = applyFilters(presentationNotes, filter, query)
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

    private fun setSelectedTab(tabTitle: String) {
        _state.value = _state.value.copy(selectedTabTitle = tabTitle)
    }

    private fun isVoiceNote(note: NotePresentationModel): Boolean {
        return note.recordingPath.isNotEmpty()
    }

    private fun isStarred(note: NotePresentationModel): Boolean {
        return note.isStarred
    }
}
