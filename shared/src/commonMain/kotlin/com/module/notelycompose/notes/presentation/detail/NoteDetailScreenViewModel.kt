package com.module.notelycompose.notes.presentation.detail

import com.module.notelycompose.core.toCommonStateFlow
import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetLastNote
import com.module.notelycompose.notes.domain.GetNoteById
import com.module.notelycompose.notes.domain.InsertNoteUseCase
import com.module.notelycompose.notes.domain.UpdateNoteUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class NoteDetailScreenViewModel(
    private val getNoteByIdUseCase: GetNoteById,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteById,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val getLastNoteUseCase: GetLastNote,
    coroutineScope: CoroutineScope? = null
) {
    private val noteDetailScreenUiState = NoteDetailScreenUiState(
        title = null,
        content = null,
        newNoteContentDate = null,
        newNoteId = 0
    )
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(noteDetailScreenUiState)

    var state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        noteDetailScreenUiState
    ).toCommonStateFlow()

    fun getNoteById(id: String) = getNoteByIdUseCase.execute(id)

    private fun getLastNote() = getLastNoteUseCase.execute()

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

    fun onCreateOrUpdateEvent(
        title: String,
        content: String,
        isUpdate: Boolean
    ) {
        when {
            isUpdate -> {
                val lastNoteId = getLastNote()?.id ?: 0
                onEvent(NoteDetailScreenEvent.UpdateNote(lastNoteId, title, content))
            }
            else -> onEvent(NoteDetailScreenEvent.NoteSaved(title, content))
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
            is NoteDetailScreenEvent.UpdateNote -> {
                viewModelScope.launch {
                    updateNoteUseCase.execute(event.id, event.title, event.content)
                }
            }
        }
    }

    // TODO: use NoteDetailScreenUiState to set the state
    fun getNewNoteContentDate(id: String): String {
        val note = getNoteById(id)
        val localDate = note?.createdAt ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val day = localDate.dayOfMonth
        val month = localDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
        val year = localDate.year
        val hour = localDate.hour
        val minute = localDate.minute.toString().padStart(2, '0')
        return "$day $month $year at $hour:$minute"
    }
}