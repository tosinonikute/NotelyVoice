package com.module.notelycompose.notes.presentation.list.model

data class NoteUiModel(
    val id: Long,
    val title: String,
    val content: String,
    val isStarred: Boolean,
    val isVoice: Boolean,
    val createdAt: String,
    val words: Int
)
