package com.module.notelycompose.notes.domain

import com.module.notelycompose.notes.domain.mapper.NoteDomainMapper
import com.module.notelycompose.notes.domain.mapper.TextFormatMapper
import com.module.notelycompose.notes.domain.model.TextAlignDomainModel
import com.module.notelycompose.notes.domain.model.TextFormatDomainModel

class UpdateNoteUseCase(
    private val noteDataSource: NoteDataSource,
    private val textFormatMapper: TextFormatMapper,
    private val noteDomainMapper: NoteDomainMapper
) {
    suspend fun execute(
        id: Long,
        title: String,
        content: String,
        formatting: List<TextFormatDomainModel>,
        textAlign: TextAlignDomainModel
    ) {
        noteDataSource.updateNote(
            id = id,
            title = title,
            content = content,
            formatting = formatting.map { textFormatMapper.mapToDataModel(it) },
            textAlign = noteDomainMapper.mapTextAlignToDataModel(textAlign)
        )
    }
}
