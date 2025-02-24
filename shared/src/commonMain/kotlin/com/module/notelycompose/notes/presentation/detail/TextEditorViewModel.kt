package com.module.notelycompose.notes.presentation.detail

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetLastNote
import com.module.notelycompose.notes.domain.GetNoteById
import com.module.notelycompose.notes.domain.InsertNoteUseCase
import com.module.notelycompose.notes.domain.UpdateNoteUseCase
import com.module.notelycompose.notes.presentation.detail.userinterface.EditorUiState
import com.module.notelycompose.notes.presentation.helpers.TextFormatHelper.updateFormats
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private const val TEXT_SIZE_TITLE = 24f
private const val TEXT_SIZE_HEADING = 20f
private const val TEXT_SIZE_SUBHEADING = 16f
private const val TEXT_SIZE_BODY = 14f
private const val ID_NOT_SET = 0L

data class TextPresentationFormat(
    val range: IntRange,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isUnderline: Boolean = false,
    val textSize: Float? = null
)

data class EditorPresentationState(
    val content: TextFieldValue = TextFieldValue(""),
    val formats: List<TextPresentationFormat> = emptyList(),
    val textAlign: TextAlign = TextAlign.Left,
    val selectionSize: TextFormatPresentationOption = TextPresentationFormats.NoSelection
)

data class TextFormatPresentationOption(
    val size: Float
)

object TextPresentationFormats {
    val Title = TextFormatPresentationOption(TEXT_SIZE_TITLE)
    val Heading = TextFormatPresentationOption(TEXT_SIZE_HEADING)
    val SubHeading = TextFormatPresentationOption(TEXT_SIZE_SUBHEADING)
    val Body = TextFormatPresentationOption(TEXT_SIZE_BODY)
    val NoSelection = TextFormatPresentationOption(0f)
}

class TextEditorViewModel (
    private val getNoteByIdUseCase: GetNoteById,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteById,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val getLastNoteUseCase: GetLastNote,
    private val editorPresentationToUiStateMapper: EditorPresentationToUiStateMapper,
    private val textFormatPresentationMapper: TextFormatPresentationMapper,
    private val textAlignPresentationMapper: TextAlignPresentationMapper,
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
                        loadNote(
                            content = retrievedNote.content,
                            formats = retrievedNote.formatting.map {
                                textFormatPresentationMapper.mapToPresentationModel(it) },
                            textAlign = textAlignPresentationMapper.mapToComposeTextAlign(
                                retrievedNote.textAlign)
                        )
                        _currentNoteId.value = id
                    }
                }
        }
    }

    fun onGetNoteById(id: String) {
        _noteIdTrigger.value = id.toLong()
    }

    private fun getNoteById(id: String) = getNoteByIdUseCase.execute(id.toLong())

    private fun getLastNote() = getLastNoteUseCase.execute()

    fun onUpdateContent(newContent: TextFieldValue) {
        updateContent(newContent)
        createOrUpdateEvent(
            title = newContent.text,
            content = newContent.text,
            isEditingStarted = isEditingStarted,
            formatting = _editorPresentationState.value.formats,
            textAlign = _editorPresentationState.value.textAlign
        )
        isEditingStarted = true
    }

    private fun loadNote(
        content: String,
        formats: List<TextPresentationFormat>,
        textAlign: TextAlign
    ) {
        _editorPresentationState.update {
            it.copy(
                content = TextFieldValue(content),
                formats = formats,
                textAlign = textAlign
            )
        }
    }

    fun onGetUiState(presentationState: EditorPresentationState): EditorUiState {
        return editorPresentationToUiStateMapper.mapToUiState(presentationState)
    }

    private fun insertNote(
        title: String,
        content: String,
        formatting: List<TextPresentationFormat>,
        textAlign: TextAlign
    ) {
        viewModelScope.launch {
            insertNoteUseCase.execute(
                title = title,
                content = content,
                formatting = formatting.map { textFormatPresentationMapper.mapToDomainModel(it) },
                textAlign = textAlignPresentationMapper.mapToDomainModel(textAlign)
            )
        }
    }

    private fun updateNote(
        noteId: Long,
        title: String,
        content: String,
        formatting: List<TextPresentationFormat>,
        textAlign: TextAlign
    ) {
        viewModelScope.launch {
            updateNoteUseCase.execute(
                id = noteId,
                title = title,
                content = content,
                formatting = formatting.map { textFormatPresentationMapper.mapToDomainModel(it) },
                textAlign = textAlignPresentationMapper.mapToDomainModel(textAlign)
            )
        }
    }

    private fun deleteNote(id: Long) {
        viewModelScope.launch {
            deleteNoteUseCase.execute(id)
        }
    }

    // TODO: use state to set this
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

    private fun createOrUpdateEvent(
        title: String,
        content: String,
        isEditingStarted: Boolean,
        formatting: List<TextPresentationFormat>,
        textAlign: TextAlign
    ) {
        val currentNoteId = _currentNoteId.value
        when {
            content.isEmpty() && isEditingStarted -> {
                val lastNoteId = getLastNote()?.id ?: 0L
                deleteNote(lastNoteId)
            }
            currentNoteId != null && _currentNoteId.value != ID_NOT_SET -> {
                updateNote(
                    noteId = currentNoteId,
                    title = title,
                    content = content,
                    formatting = formatting,
                    textAlign = textAlign
                )
            }
            else -> {
                insertNote(
                    title = title,
                    content = content,
                    formatting = formatting,
                    textAlign = textAlign
                ).also {
                    _currentNoteId.value = getLastNote()?.id ?: 0L
                }
            }
        }
    }

    private fun updateContent(newContent: TextFieldValue) {
        try {
            val oldText = _editorPresentationState.value.content.text
            val newText = newContent.text
            val selection = newContent.selection

            val updatedFormats = _editorPresentationState.value.formats
                .updateFormats(oldText, newText, selection.start)

            // Handle Enter key press and bullet points
            if (newText.length > oldText.length && selection.start > 0 &&
                selection.start <= newText.length &&
                newText[selection.start - 1] == '\n'
            ) {
                val bulletResult = handleBulletListContinuation(newText, selection, updatedFormats)
                if (bulletResult != null) {
                    _editorPresentationState.update {
                        bulletResult.copy(
                            selectionSize = getSizeLabel(newContent, updatedFormats)
                        )
                    }
                    return
                }
            }

            _editorPresentationState.update { currentState ->
                currentState.copy(
                    content = newContent,
                    formats = updatedFormats,
                    selectionSize = getSizeLabel(newContent, updatedFormats)
                )
            }
        } catch (e: Exception) {
            _editorPresentationState.update {
                it.copy(
                    content = newContent,
                    selectionSize = getSizeLabel(newContent, it.formats)
                )
            }
        }
    }

    private fun handleBulletListContinuation(
        newText: String,
        selection: TextRange,
        updatedFormats: List<TextPresentationFormat>
    ): EditorPresentationState? {
        val previousLineEnd = (selection.start - 1).coerceIn(0, newText.length)
        val textBeforeCursor = newText.substring(0, previousLineEnd)
        val lastNewLineIndex = textBeforeCursor.lastIndexOf('\n')
        val previousLineStart = if (lastNewLineIndex == -1) 0 else (lastNewLineIndex + 1)
        val previousLine = textBeforeCursor.substring(previousLineStart, previousLineEnd)

        // Check if the previous line was an empty bullet point
        if (previousLine.trim() == "•" || previousLine.trim() == "• ") {
            // Remove the empty bullet point and add a new line
            val textWithoutEmptyBullet = newText.substring(0, previousLineStart) +
                    "\n" +
                    newText.substring(selection.start)

            return _editorPresentationState.value.copy(
                content = TextFieldValue(
                    text = textWithoutEmptyBullet,
                    selection = TextRange(previousLineStart + 1)
                ),
                formats = updatedFormats
            )
        }

        // Handle normal bullet point continuation
        if (previousLine.trimStart().startsWith("• ")) {
            val indentation = previousLine.takeWhile { it.isWhitespace() }
            val beforeCursor = newText.substring(0, selection.start)
            val afterCursor = if (selection.start < newText.length) {
                newText.substring(selection.start)
            } else ""

            val textWithNewBullet = beforeCursor + indentation + "• " + afterCursor
            val newCursorPosition = (selection.start + indentation.length + 2)
                .coerceIn(0, textWithNewBullet.length)

            return _editorPresentationState.value.copy(
                content = TextFieldValue(
                    text = textWithNewBullet,
                    selection = TextRange(newCursorPosition)
                ),
                formats = updatedFormats
            )
        }

        return null
    }

    fun onToggleBold() {
        toggleFormat { it.copy(isBold = !it.isBold) }
        refreshSelection()
    }

    fun onToggleItalic() {
        toggleFormat { it.copy(isItalic = !it.isItalic) }
        refreshSelection()
    }

    fun setTextSize(size: Float) {
        toggleFormat { it.copy(textSize = size) }
        refreshSelection()
    }

    fun onToggleUnderline() {
        toggleFormat { it.copy(isUnderline = !it.isUnderline) }
        refreshSelection()
    }

    private fun refreshSelection() {
        _editorPresentationState.update { current ->
            current.copy(
                content = current.content.copy(
                    selection = TextRange(
                        current.content.selection.start,
                        current.content.selection.end
                    )
                )
            )
        }
    }

    private fun getSizeLabel(
        content: TextFieldValue,
        formats: List<TextPresentationFormat>
    ): TextFormatPresentationOption {
        return if (content.selection.start == content.selection.end) {
            TextPresentationFormats.NoSelection
        } else {
            formats.find { it.range.contains(content.selection.start) }
                ?.textSize?.let { size ->
                    when (size) {
                        24f -> TextPresentationFormats.Title
                        20f -> TextPresentationFormats.Heading
                        16f -> TextPresentationFormats.SubHeading
                        else -> TextPresentationFormats.Body
                    }
                } ?: TextPresentationFormats.Body
        }
    }

    private fun toggleFormat(transform: (TextPresentationFormat) -> TextPresentationFormat) {
        val selection = _editorPresentationState.value.content.selection
        if (selection.start == selection.end) return

        val start = selection.start.coerceIn(0, _editorPresentationState.value.content.text.length)
        val end = selection.end.coerceIn(0, _editorPresentationState.value.content.text.length)

        _editorPresentationState.update { currentState ->
            val existingFormat = currentState.formats.find {
                it.range.contains(start) && it.range.contains(end - 1)
            }

            val newFormat = transform(existingFormat ?: TextPresentationFormat(start..end))

            currentState.copy(
                formats = currentState.formats.filter {
                    !it.range.overlaps(start..end)
                } + newFormat
            )
        }
    }

    fun onSetAlignment(alignment: TextAlign) {
        _editorPresentationState.update { it.copy(textAlign = alignment) }
        val content = _editorPresentationState.value.content
        val formats = _editorPresentationState.value.formats
        val textAlign = _editorPresentationState.value.textAlign
        if(content.text.isNotEmpty()) {
            createOrUpdateEvent(
                title = content.text,
                content = content.text,
                isEditingStarted = true,
                formatting = formats,
                textAlign = textAlign
            )
        }
    }

    fun onToggleBulletList() {
        val currentState = _editorPresentationState.value
        val selection = currentState.content.selection
        val text = currentState.content.text

        try {
            // Handle case when no text is selected
            if (selection.start == selection.end) {
                // Get the current line
                val lineStart = text.lastIndexOf('\n', selection.start - 1).let {
                    if (it == -1) 0 else it + 1
                }
                val lineEnd = text.indexOf('\n', selection.start).let {
                    if (it == -1) text.length else it
                }
                val currentLine = text.substring(lineStart, lineEnd)

                // Create new text with bullet point
                val newText = buildString {
                    append(text.substring(0, lineStart))
                    // Only add bullet if line doesn't already have one
                    if (!currentLine.trimStart().startsWith("• ")) {
                        append("• ")
                        append(currentLine)
                    } else {
                        append(currentLine.replaceFirst("• ", ""))
                    }
                    if (lineEnd < text.length) {
                        append(text.substring(lineEnd))
                    }
                }

                val newCursorPosition = if (!currentLine.trimStart().startsWith("• ")) {
                    lineStart + 2 + currentLine.length
                } else {
                    lineStart + currentLine.length - 2
                }

                _editorPresentationState.update { currentState ->
                    currentState.copy(
                        content = TextFieldValue(
                            text = newText,
                            selection = TextRange(newCursorPosition)
                        )
                    )
                }
                return
            }

            // Original logic for selected text
            val selectedText = text.substring(selection.start, selection.end)
            val lines = selectedText.split("\n")

            val processedLines = lines.map { line ->
                if (line.trim().startsWith("• ")) {
                    line.replaceFirst("• ", "")
                } else if (line.isNotEmpty()) {
                    "• $line"
                } else {
                    line
                }
            }

            val newText = buildString {
                append(text.substring(0, selection.start))
                append(processedLines.joinToString("\n"))
                if (selection.end < text.length) {
                    append(text.substring(selection.end))
                }
            }

            _editorPresentationState.update { currentState ->
                currentState.copy(
                    content = TextFieldValue(
                        text = newText,
                        selection = TextRange(
                            selection.start,
                            (selection.start + processedLines.joinToString("\n").length)
                                .coerceIn(0, newText.length)
                        )
                    )
                )
            }
        } catch (e: Exception) {
            // If any error occurs, keep the current state
            return
        }
    }

    private fun IntRange.overlaps(other: IntRange): Boolean =
        first <= other.last && other.first <= last
}