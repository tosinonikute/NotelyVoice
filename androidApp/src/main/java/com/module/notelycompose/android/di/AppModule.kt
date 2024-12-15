package com.module.notelycompose.android.di

import android.app.Application
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
        return NoteSqlDelightDataSource(db = NoteDatabase(driver))
    }

    @Provides
    @Singleton
    fun provideGetAllNotesUseCase(
        dataSource: NoteDataSource
    ): GetAllNotesUseCase {
        return GetAllNotesUseCase(dataSource)
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
        dataSource: NoteDataSource
    ): InsertNoteUseCase {
        return InsertNoteUseCase(dataSource)
    }

    @Provides
    @Singleton
    fun provideGetNoteByIdUseCase(
        dataSource: NoteDataSource
    ): GetNoteById {
        return GetNoteById(dataSource)
    }

    @Provides
    @Singleton
    fun provideGetLastNoteUseCase(
        dataSource: NoteDataSource
    ): GetLastNote {
        return GetLastNote(dataSource)
    }

    @Provides
    @Singleton
    fun provideUpdateNoteUseCase(
        dataSource: NoteDataSource
    ): UpdateNoteUseCase {
        return UpdateNoteUseCase(dataSource)
    }
}