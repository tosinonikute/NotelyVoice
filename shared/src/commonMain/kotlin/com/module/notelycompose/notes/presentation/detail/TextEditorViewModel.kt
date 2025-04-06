package com.module.notelycompose.notes.presentation.detail

import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetLastNote
import com.module.notelycompose.notes.domain.GetNoteById
import com.module.notelycompose.notes.domain.InsertNoteUseCase
import com.module.notelycompose.notes.domain.UpdateNoteUseCase
import com.module.notelycompose.notes.domain.model.NoteDomainModel
import com.module.notelycompose.notes.presentation.detail.model.EditorPresentationState
import com.module.notelycompose.notes.presentation.detail.model.RecordingPathPresentationModel
import com.module.notelycompose.notes.presentation.detail.model.TextPresentationFormat
import com.module.notelycompose.notes.presentation.helpers.TextEditorHelper
import com.module.notelycompose.notes.ui.detail.EditorUiState
import com.module.notelycompose.notes.presentation.mapper.EditorPresentationToUiStateMapper
import com.module.notelycompose.notes.presentation.mapper.TextAlignPresentationMapper
import com.module.notelycompose.notes.presentation.mapper.TextFormatPresentationMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private const val ID_NOT_SET = 0L

class TextEditorViewModel (
    private val getNoteByIdUseCase: GetNoteById,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteById,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val getLastNoteUseCase: GetLastNote,
    private val editorPresentationToUiStateMapper: EditorPresentationToUiStateMapper,
    private val textFormatPresentationMapper: TextFormatPresentationMapper,
    private val textAlignPresentationMapper: TextAlignPresentationMapper,
    private val textEditorHelper: TextEditorHelper,
    coroutineScope: CoroutineScope? = null
) {

    private val _editorPresentationState = MutableStateFlow(EditorPresentationState())
    val editorPresentationState: StateFlow<EditorPresentationState> = _editorPresentationState
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private var isEditingStarted = false
    private var _currentNoteId = MutableStateFlow<Long?>(ID_NOT_SET)
    private val _noteIdTrigger = MutableStateFlow<Long?>(null)

    init {
        viewModelScope.launch {
            _noteIdTrigger
                .filterNotNull()
                .take(1)
                .collect { id ->
                    val note = getNoteByIdUseCase.execute(id)
                    note?.let { retrievedNote ->
                        processNote(retrievedNote)
                        _currentNoteId.value = id
                    }
                }
        }
    }

    private fun processNote(retrievedNote: NoteDomainModel) {
        loadNote(
            content = retrievedNote.content,
            formats = retrievedNote.formatting.map {
                textFormatPresentationMapper.mapToPresentationModel(it) },
            textAlign = textAlignPresentationMapper.mapToComposeTextAlign(
                retrievedNote.textAlign),
            recordingPath = retrievedNote.recordingPath,
            starred = retrievedNote.starred,
            createdAt = getFormattedDate(retrievedNote.createdAt)
        )
    }

    fun onGetNoteById(id: String) {
        _noteIdTrigger.value = id.toLong()
    }

    private fun getLastNote() = getLastNoteUseCase.execute()

    fun onUpdateContent(newContent: TextFieldValue) {
        updateContent(newContent)
        createOrUpdateEvent(
            title = newContent.text,
            content = newContent.text,
            starred = _editorPresentationState.value.starred,
            isEditingStarted = isEditingStarted,
            formatting = _editorPresentationState.value.formats,
            textAlign = _editorPresentationState.value.textAlign,
            recordingPath = _editorPresentationState.value.recording.recordingPath,
        )
        isEditingStarted = true
    }

    fun onUpdateRecordingPath(recordingPath: String) {
        _editorPresentationState.update {
            it.copy(
                recording = recordingPath(recordingPath)
            )
        }
        onUpdateContent(newContent = _editorPresentationState.value.content)
    }

    private fun recordingPath(recordingPath: String) = RecordingPathPresentationModel(
        recordingPath = recordingPath,
        isRecordingExist = recordingPath.isNotEmpty()
    )

    private fun loadNote(
        content: String,
        formats: List<TextPresentationFormat>,
        textAlign: TextAlign,
        recordingPath: String,
        starred: Boolean,
        createdAt: String
    ) {
        _editorPresentationState.update {
            it.copy(
                content = TextFieldValue(content),
                formats = formats,
                textAlign = textAlign,
                recording = recordingPath(recordingPath),
                starred = starred,
                createdAt = createdAt
            )
        }
    }

    fun onGetUiState(presentationState: EditorPresentationState): EditorUiState {
        return editorPresentationToUiStateMapper.mapToUiState(presentationState)
    }

    private fun insertNote(
        title: String,
        content: String,
        starred: Boolean,
        formatting: List<TextPresentationFormat>,
        textAlign: TextAlign,
        recordingPath: String
    ) {
        viewModelScope.launch {
            _currentNoteId.value = insertNoteUseCase.execute(
                title = title,
                content = content,
                starred = starred,
                formatting = formatting.map { textFormatPresentationMapper.mapToDomainModel(it) },
                textAlign = textAlignPresentationMapper.mapToDomainModel(textAlign),
                recordingPath = recordingPath
            )
        }
    }

    private fun updateNote(
        noteId: Long,
        title: String,
        content: String,
        starred: Boolean,
        formatting: List<TextPresentationFormat>,
        textAlign: TextAlign,
        recordingPath: String
    ) {
        viewModelScope.launch {
            updateNoteUseCase.execute(
                id = noteId,
                title = title,
                content = content,
                starred = starred,
                formatting = formatting.map { textFormatPresentationMapper.mapToDomainModel(it) },
                textAlign = textAlignPresentationMapper.mapToDomainModel(textAlign),
                recordingPath = recordingPath
            )
        }
    }

    fun onDeleteNote() {
        _currentNoteId.value?.let { noteId ->
            deleteNote(id = noteId)
        }
    }

    private fun deleteNote(id: Long) {
        viewModelScope.launch {
            deleteNoteUseCase.execute(id)
        }
    }

    fun onToggleStar() {
        val starred = _editorPresentationState.value.starred
        _editorPresentationState.update {
            it.copy(
                starred = !starred
            )
        }
        onUpdateContent(newContent = _editorPresentationState.value.content)
    }

    private fun getFormattedDate(
        createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    ): String {
        val day = createdAt.dayOfMonth
        val month = createdAt.month.name.lowercase().replaceFirstChar { it.uppercase() }
        val year = createdAt.year
        val hour = createdAt.hour
        val minute = createdAt.minute.toString().padStart(2, '0')
        return "$day $month $year at $hour:$minute"
    }

    private fun createOrUpdateEvent(
        title: String,
        content: String,
        starred: Boolean,
        isEditingStarted: Boolean,
        formatting: List<TextPresentationFormat>,
        textAlign: TextAlign,
        recordingPath: String
    ) {
        val currentNoteId = _currentNoteId.value
        when {
            content.isEmpty() && isEditingStarted -> {
                getLastNote()?.let { lastNote ->  deleteNote(lastNote.id) }
            }
            currentNoteId != null && currentNoteId != ID_NOT_SET -> {
                updateNote(
                    noteId = currentNoteId,
                    title = title,
                    content = content,
                    starred = starred,
                    formatting = formatting,
                    textAlign = textAlign,
                    recordingPath = recordingPath
                )
            }
            else -> {
                insertNote(
                    title = title,
                    content = content,
                    starred = starred,
                    formatting = formatting,
                    textAlign = textAlign,
                    recordingPath = recordingPath
                )
            }
        }
    }

    private fun updateContent(newContent: TextFieldValue) {
        textEditorHelper.updateContent(
            newContent = newContent,
            currentState = _editorPresentationState.value,
            getFormattedDate = { getFormattedDate() },
            updateState = { newState ->
                _editorPresentationState.update { newState }
            }
        )
    }

    fun onToggleBold() {
        textEditorHelper.toggleFormat(
            currentState = _editorPresentationState.value,
            transform = { it.copy(isBold = !it.isBold) },
            updateState = { newState ->
                _editorPresentationState.update { newState }
            }
        )
        refreshSelection()
    }

    fun onToggleItalic() {
        textEditorHelper.toggleFormat(
            currentState = _editorPresentationState.value,
            transform = { it.copy(isItalic = !it.isItalic) },
            updateState = { newState ->
                _editorPresentationState.update { newState }
            }
        )
        refreshSelection()
    }

    fun setTextSize(size: Float) {
        textEditorHelper.toggleFormat(
            currentState = _editorPresentationState.value,
            transform = { it.copy(textSize = size) },
            updateState = { newState ->
                _editorPresentationState.update { newState }
            }
        )
        refreshSelection()
    }

    fun onToggleUnderline() {
        textEditorHelper.toggleFormat(
            currentState = _editorPresentationState.value,
            transform = { it.copy(isUnderline = !it.isUnderline) },
            updateState = { newState ->
                _editorPresentationState.update { newState }
            }
        )
        refreshSelection()
    }

    private fun refreshSelection() {
        textEditorHelper.refreshSelection(
            currentState = _editorPresentationState.value,
            updateState = { newState ->
                _editorPresentationState.update { newState }
            }
        )
    }

    fun onSetAlignment(alignment: TextAlign) {
        _editorPresentationState.update { it.copy(textAlign = alignment) }
        val content = _editorPresentationState.value.content
        val formats = _editorPresentationState.value.formats
        val textAlign = _editorPresentationState.value.textAlign
        val starred = _editorPresentationState.value.starred
        val recordingPath = _editorPresentationState.value.recording.recordingPath
        if(content.text.isNotEmpty()) {
            createOrUpdateEvent(
                title = content.text,
                content = content.text,
                starred = starred,
                isEditingStarted = true,
                formatting = formats,
                textAlign = textAlign,
                recordingPath = recordingPath
            )
        }
    }

    fun onToggleBulletList() {
        textEditorHelper.toggleBulletList(
            currentState = _editorPresentationState.value,
            updateState = { newState ->
                _editorPresentationState.update { newState }
            }
        )
    }
}
