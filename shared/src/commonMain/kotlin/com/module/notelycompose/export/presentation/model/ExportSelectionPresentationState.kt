package com.module.notelycompose.export.presentation.model

import com.module.notelycompose.notes.presentation.list.model.NotePresentationModel

data class ExportSelectionPresentationState(
    val noteIds: List<Long> = emptyList(),
    val shouldExportAudio: Boolean = false,
    val shouldExportTxt: Boolean = false,
    val shouldExportMd: Boolean = false,
    val selectedNotes: List<NotePresentationModel> = emptyList()
)
