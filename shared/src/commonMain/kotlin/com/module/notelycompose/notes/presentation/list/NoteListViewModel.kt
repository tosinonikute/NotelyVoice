package com.module.notelycompose.notes.presentation.list

import com.module.notelycompose.audio.ui.expect.deleteFile
import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetAllNotesUseCase
import com.module.notelycompose.notes.domain.SearchNotesUseCase
import com.module.notelycompose.notes.domain.model.NoteDomainModel
import com.module.notelycompose.notes.domain.model.NotesFilterDomainModel
import com.module.notelycompose.notes.domain.model.NotesFilterDomainModel.ALL
import com.module.notelycompose.notes.presentation.helpers.getFirstNonEmptyLineAfterFirst
import com.module.notelycompose.notes.presentation.helpers.returnFirstLine
import com.module.notelycompose.notes.presentation.helpers.truncateWithEllipsis
import com.module.notelycompose.notes.presentation.list.mapper.NotesFilterMapper
import com.module.notelycompose.notes.presentation.mapper.NotePresentationMapper
import com.module.notelycompose.notes.ui.list.model.NoteUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import notelycompose.shared.generated.resources.Res

const val DEFAULT_TITLE = "New Note"
const val DEFAULT_CONTENT = "No additional text"
const val CONTENT_LENGTH = 36

class NoteListViewModel(
    private val selectedTabTitle:String,
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val searchNotesUseCase: SearchNotesUseCase,
    private val deleteNoteById: DeleteNoteById,
    private val notePresentationMapper: NotePresentationMapper,
    private val notesFilterMapper: NotesFilterMapper,
    coroutineScope: CoroutineScope? = null
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(NoteListPresentationState(selectedTabTitle = selectedTabTitle))
    val state: StateFlow<NoteListPresentationState> = _state

    init {
        val presentationFilter = notesFilterMapper.mapStringToPresentationModel(state.value.selectedTabTitle)
        val domainFilter = notesFilterMapper.mapToDomainModel(presentationFilter)
        createCombinedState(domainFilter)
    }

    fun onProcessIntent(intent: NoteListIntent) {
        when (intent) {
            is NoteListIntent.OnNoteDeleted -> {
                viewModelScope.launch {
                    deleteFile(intent.note.recordingPath)
                    deleteNoteById.execute(intent.note.id)
                    createCombinedState(ALL)
                }
            }
            is NoteListIntent.OnFilterNote -> {
                val presentationFilter = notesFilterMapper.mapStringToPresentationModel(intent.filter)
                val domainFilter = notesFilterMapper.mapToDomainModel(presentationFilter)
                setSelectedTab(intent.filter)
                createCombinedState(domainFilter)
            }
            is NoteListIntent.OnSearchNote -> {
                searchNotesState(intent.keyword)
            }
        }
    }

    private fun createCombinedState(
        filter: NotesFilterDomainModel
    ) {
        viewModelScope.launch {
            getAllNotesUseCase.execute(filter).collect { notes ->
                setNoteState(notes)
            }
        }
    }

    private fun searchNotesState(
        keyword: String
    ) {
        if(keyword.isNotEmpty()) {
            viewModelScope.launch {
                searchNotesUseCase.execute(keyword).collect { notes ->
                    setNoteState(notes)
                }
            }
        } else {
            createCombinedState(ALL)
        }
    }

    private fun setNoteState(notes: List<NoteDomainModel>) {
        _state.value = _state.value.copy(
            notes = notes.map { note ->
                val retrievedNote = notePresentationMapper.mapToPresentationModel(note)
                retrievedNote.copy(
                    title = if (note.title.trim().isEmpty()){
                        DEFAULT_TITLE
                    } else {
                        note.title
                            .returnFirstLine()
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

    fun onGetUiState(presentationState: NoteListPresentationState): List<NoteUiModel> {
        return presentationState.notes.map { notePresentationMapper.mapToUiModel(it) }
    }

    private fun setSelectedTab(tabTitle: String) {
        _state.value = _state.value.copy(selectedTabTitle = tabTitle)
    }
}
