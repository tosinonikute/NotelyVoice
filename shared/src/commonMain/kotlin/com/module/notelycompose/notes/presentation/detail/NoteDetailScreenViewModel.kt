package com.module.notelycompose.notes.presentation.detail

import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import com.module.notelycompose.core.toCommonStateFlow
import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetLastNote
import com.module.notelycompose.notes.domain.GetNoteById
import com.module.notelycompose.notes.domain.InsertNoteUseCase
import com.module.notelycompose.notes.domain.UpdateNoteUseCase
import com.module.notelycompose.notes.presentation.mapper.TextAlignPresentationMapper
import com.module.notelycompose.notes.presentation.mapper.TextFormatPresentationMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    private val textFormatPresentationMapper: TextFormatPresentationMapper,
    private val textAlignPresentationMapper: TextAlignPresentationMapper,
    coroutineScope: CoroutineScope? = null
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private fun getNoteById(id: String) = getNoteByIdUseCase.execute(id.toLong())

    private fun getLastNote() = getLastNoteUseCase.execute()

    fun onCreateOrUpdateEvent(
        newContent: TextFieldValue,
        oldContentText: String,
        oldFormats: List<TextPresentationFormat>,
        isUpdate: Boolean,
        textAlign: TextAlign
    ) {
        val updatedFormats = updateFormats(
            formats = oldFormats,
            oldText = oldContentText,
            newText = newContent.text,
            changePosition = newContent.selection.start
        )
        createOrUpdateEvent(
            title = newContent.text,
            content = newContent.text,
            isUpdate = isUpdate,
            formatting = updatedFormats,
            textAlign = textAlign
        )
    }

    private fun createOrUpdateEvent(
        title: String,
        content: String,
        isUpdate: Boolean,
        formatting: List<TextPresentationFormat>,
        textAlign: TextAlign
    ) {
        when {
            content.isEmpty() && isUpdate -> {
                val lastNoteId = getLastNote()?.id ?: 0L
                onEvent(NoteDetailScreenEvent.ClearNoteOnEmptyContent(lastNoteId.toString()))
            }
            isUpdate -> {
                val lastNoteId = getLastNote()?.id ?: 0L
                onEvent(NoteDetailScreenEvent
                    .UpdateNote(lastNoteId, title, content, formatting, textAlign)
                )
            }
            else -> onEvent(NoteDetailScreenEvent
                .NoteSaved(title, content, formatting, textAlign)
            )
        }
    }

    fun onEvent(event: NoteDetailScreenEvent) {
        when (event) {
            is NoteDetailScreenEvent.NoteSaved -> {
                insertNote(
                    title = event.title,
                    content = event.content,
                    starred = true,
                    formatting = event.formatting,
                    textAlign = event.textAlign,
                    recordingPath = ""
                )
            }
            is NoteDetailScreenEvent.DeleteNote -> {
                deleteNote(event.id.toLong())
            }
            is NoteDetailScreenEvent.UpdateNote -> {
                updateNote(
                    noteId = event.id,
                    title = event.title,
                    content = event.content,
                    starred = true,
                    formatting = event.formatting,
                    textAlign = event.textAlign
                )
            }
            is NoteDetailScreenEvent.ClearNoteOnEmptyContent -> {
                deleteNote(event.id.toLong())
            }
        }
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
            insertNoteUseCase.execute(
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
        textAlign: TextAlign
    ) {
        viewModelScope.launch {
            updateNoteUseCase.execute(
                id = noteId,
                title = title,
                content = content,
                starred = starred,
                formatting = formatting.map { textFormatPresentationMapper.mapToDomainModel(it) },
                textAlign = textAlignPresentationMapper.mapToDomainModel(textAlign),
                recordingPath = ""
            )
        }
    }

    private fun deleteNote(id: Long) {
        viewModelScope.launch {
            deleteNoteUseCase.execute(id)
        }
    }

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

    private fun updateFormats(
        formats: List<TextPresentationFormat>,
        oldText: String,
        newText: String,
        changePosition: Int
    ): List<TextPresentationFormat> {
        val lengthDiff = newText.length - oldText.length
        return formats.mapNotNull { format ->
            when {
                changePosition <= format.range.first -> {
                    val newStart = (format.range.first + lengthDiff).coerceAtLeast(0)
                    val newEnd = (format.range.last + lengthDiff).coerceAtLeast(newStart)
                    if (newStart < newEnd) {
                        format.copy(range = newStart..newEnd)
                    } else null
                }
                changePosition < format.range.last -> {
                    val newEnd = (format.range.last + lengthDiff).coerceAtLeast(format.range.first)
                    format.copy(range = format.range.first..newEnd)
                }
                else -> format
            }
        }
    }
}