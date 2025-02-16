package com.module.notelycompose

import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetLastNote
import com.module.notelycompose.notes.domain.GetNoteById
import com.module.notelycompose.notes.domain.InsertNoteUseCase
import com.module.notelycompose.notes.domain.UpdateNoteUseCase
import com.module.notelycompose.notes.presentation.detail.EditorPresentationState
import com.module.notelycompose.notes.presentation.detail.TextEditorViewModel
import com.module.notelycompose.notes.presentation.detail.userinterface.EditorUiState
import com.module.notelycompose.notes.presentation.mapper.EditorPresentationToUiStateMapper
import com.module.notelycompose.notes.presentation.mapper.TextAlignPresentationMapper
import com.module.notelycompose.notes.presentation.mapper.TextFormatPresentationMapper

class IOSTextEditorViewModel(
    private val getNoteByIdUseCase: GetNoteById,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteById,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val getLastNoteUseCase: GetLastNote,
    private val editorPresentationToUiStateMapper: EditorPresentationToUiStateMapper,
    private val textFormatPresentationMapper: TextFormatPresentationMapper,
    private val textAlignPresentationMapper: TextAlignPresentationMapper
) {

    private val viewModel by lazy {
        TextEditorViewModel(
            getNoteByIdUseCase = getNoteByIdUseCase,
            insertNoteUseCase = insertNoteUseCase,
            deleteNoteUseCase = deleteNoteUseCase,
            updateNoteUseCase = updateNoteUseCase,
            getLastNoteUseCase = getLastNoteUseCase,
            editorPresentationToUiStateMapper = editorPresentationToUiStateMapper,
            textFormatPresentationMapper = textFormatPresentationMapper,
            textAlignPresentationMapper =  textAlignPresentationMapper
        )
    }
    val state = viewModel.editorPresentationState

    fun onGetNoteById(id: String) {
        viewModel.onGetNoteById(id)
    }

    fun onGetUiState(presentationState: EditorPresentationState): EditorUiState {
        return viewModel.onGetUiState(presentationState)
    }

    fun onUpdateContent(
        isExistingNote: Boolean,
        newContent: TextFieldValue
    ) {
        return viewModel.onUpdateContent(isExistingNote, newContent)
    }

    fun onToggleBold() {
        return viewModel.onToggleBold()
    }

    fun onToggleItalic() {
        return viewModel.onToggleItalic()
    }

    fun setTextSize(size: Float) {
        return viewModel.setTextSize(size)
    }

    fun onToggleUnderline() {
        return viewModel.onToggleUnderline()
    }

    fun onSetAlignment(alignment: TextAlign) {
        return viewModel.onSetAlignment(alignment)
    }

    fun onToggleBulletList() {
        return viewModel.onToggleBulletList()
    }

    // TODO: use state to set this
    fun getNewNoteContentDate(id: String): String {
        return viewModel.getNewNoteContentDate(id)
    }
}
