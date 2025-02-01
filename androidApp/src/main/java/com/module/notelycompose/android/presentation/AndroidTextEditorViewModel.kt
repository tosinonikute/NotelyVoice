package com.module.notelycompose.android.presentation

import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModel
import com.module.notelycompose.notes.presentation.detail.EditorPresentationState
import com.module.notelycompose.notes.presentation.detail.TextEditorViewModel
import com.module.notelycompose.notes.presentation.detail.userinterface.EditorUiState
import com.module.notelycompose.notes.presentation.mapper.EditorPresentationToUiStateMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidTextEditorViewModel @Inject constructor() : ViewModel() {

    private val mapper by lazy { EditorPresentationToUiStateMapper() }

    private val viewModel by lazy {
        TextEditorViewModel(
            mapper = mapper
        )
    }
    val state = viewModel.editorPresentationState

    fun onGetUiState(presentationState: EditorPresentationState): EditorUiState {
        return viewModel.onGetUiState(presentationState)
    }

    fun onUpdateContent(newContent: TextFieldValue) {
        return viewModel.onUpdateContent(newContent)
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
}