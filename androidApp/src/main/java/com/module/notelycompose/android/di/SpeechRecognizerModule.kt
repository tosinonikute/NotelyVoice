package com.module.notelycompose.android.di

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import com.module.notelycompose.audio.presentation.mappers.AudioRecorderPresentationToUiMapper
import com.module.notelycompose.audio.ui.expect.AudioRecorder
import com.module.notelycompose.audio.ui.expect.SpeechRecognizer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SpeechRecognizerModule {



    @Singleton
    class SpeechRecognizerFactory @Inject constructor(
        @ApplicationContext private val context: Context,
    ) {
        fun create(): SpeechRecognizer {
            return SpeechRecognizer(
                context = context,
            )
        }
    }

    @Provides
    @Singleton
    fun provideSpeechRecognizerFactory(
        @ApplicationContext context: Context,
    ): SpeechRecognizerFactory {
        return SpeechRecognizerFactory(context)
    }

    @Provides
    fun provideSpeechRecognizer(factory: SpeechRecognizerFactory): SpeechRecognizer {
        return factory.create()
    }
}
