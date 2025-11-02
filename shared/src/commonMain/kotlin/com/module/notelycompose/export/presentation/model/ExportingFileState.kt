package com.module.notelycompose.export.presentation.model

sealed interface ExportingFileState {
    object Idle : ExportingFileState
    data class Exporting(val progress: Float = 0f) : ExportingFileState
    object Success : ExportingFileState
    data class Failure(val message: String) : ExportingFileState
    object NoFolderSelected : ExportingFileState
}
