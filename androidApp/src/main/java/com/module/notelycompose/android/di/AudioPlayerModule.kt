package com.module.notelycompose.android.di

import com.module.notelycompose.audio.ui.expect.PlatformAudioPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudioPlayerModule {

    @Provides
    @Singleton
    fun providePlatformAudioPlayer(): PlatformAudioPlayer {
        return PlatformAudioPlayer()
    }
}
