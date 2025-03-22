package com.module.notelycompose.android.di

import android.app.Application
import com.module.notelycompose.audio.presentation.mappers.AudioRecorderPresentationToUiStateMapper
import com.module.notelycompose.core.DatabaseDriverFactory
import com.module.notelycompose.database.NoteDatabase
import com.module.notelycompose.notes.data.NoteSqlDelightDataSource
import com.module.notelycompose.notes.domain.DeleteNoteById
import com.module.notelycompose.notes.domain.GetAllNotesUseCase
import com.module.notelycompose.notes.domain.GetLastNote
import com.module.notelycompose.notes.domain.GetNoteById
import com.module.notelycompose.notes.domain.InsertNoteUseCase
import com.module.notelycompose.notes.domain.NoteDataSource
import com.module.notelycompose.notes.domain.UpdateNoteUseCase
import com.module.notelycompose.notes.domain.mapper.NoteDomainMapper
import com.module.notelycompose.notes.domain.mapper.TextFormatMapper
import com.module.notelycompose.notes.presentation.mapper.EditorPresentationToUiStateMapper
import com.module.notelycompose.notes.presentation.mapper.NoteUiMapper
import com.module.notelycompose.notes.presentation.mapper.TextAlignPresentationMapper
import com.module.notelycompose.notes.presentation.mapper.TextFormatPresentationMapper
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabaseDriver(app: Application): SqlDriver {
        return DatabaseDriverFactory(app).create()
    }

    @Provides
    @Singleton
    fun provideNotesDataSource(driver: SqlDriver): NoteDataSource {
        return NoteSqlDelightDataSource(
            database = NoteDatabase(driver)
        )
    }

    @Provides
    @Singleton
    fun provideGetAllNotesUseCase(
        dataSource: NoteDataSource,
        noteDomainMapper: NoteDomainMapper
    ): GetAllNotesUseCase {
        return GetAllNotesUseCase(dataSource, noteDomainMapper)
    }

    @Provides
    @Singleton
    fun provideDeleteNoteByIdUseCase(
        dataSource: NoteDataSource
    ): DeleteNoteById {
        return DeleteNoteById(dataSource)
    }

    @Provides
    @Singleton
    fun provideInsertNoteUseCase(
        dataSource: NoteDataSource,
        textFormatMapper: TextFormatMapper,
        noteDomainMapper: NoteDomainMapper
    ): InsertNoteUseCase {
        return InsertNoteUseCase(
            dataSource,
            textFormatMapper,
            noteDomainMapper
        )
    }

    @Provides
    @Singleton
    fun provideGetNoteByIdUseCase(
        dataSource: NoteDataSource,
        noteDomainMapper: NoteDomainMapper
    ): GetNoteById {
        return GetNoteById(dataSource, noteDomainMapper)
    }

    @Provides
    @Singleton
    fun provideGetLastNoteUseCase(
        dataSource: NoteDataSource,
        noteDomainMapper: NoteDomainMapper
    ): GetLastNote {
        return GetLastNote(dataSource, noteDomainMapper)
    }

    @Provides
    @Singleton
    fun provideUpdateNoteUseCase(
        dataSource: NoteDataSource,
        textFormatMapper: TextFormatMapper,
        noteDomainMapper: NoteDomainMapper
    ): UpdateNoteUseCase {
        return UpdateNoteUseCase(
            dataSource,
            textFormatMapper,
            noteDomainMapper
        )
    }

    @Provides
    @Singleton
    fun provideTextFormatMapper(): TextFormatMapper {
        return TextFormatMapper()
    }

    @Provides
    @Singleton
    fun provideNoteMapper(): NoteDomainMapper {
        return NoteDomainMapper(textFormatMapper = TextFormatMapper())
    }

    @Provides
    @Singleton
    fun provideNoteUiMapper(): NoteUiMapper {
        return NoteUiMapper()
    }

    @Provides
    @Singleton
    fun provideEditorPresentationToUiStateMapper(): EditorPresentationToUiStateMapper {
        return EditorPresentationToUiStateMapper()
    }

    @Provides
    @Singleton
    fun provideTextFormatPresentationMapper(): TextFormatPresentationMapper {
        return TextFormatPresentationMapper()
    }

    @Provides
    @Singleton
    fun provideTextAlignPresentationMapper(): TextAlignPresentationMapper {
        return TextAlignPresentationMapper()
    }

    @Provides
    @Singleton
    fun provideAudioRecorderPresentationToUiStateMapper(): AudioRecorderPresentationToUiStateMapper {
        return AudioRecorderPresentationToUiStateMapper()
    }
}
