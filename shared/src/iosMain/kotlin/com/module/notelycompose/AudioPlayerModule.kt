package com.module.notelycompose

import com.module.notelycompose.audio.presentation.mappers.AudioPlayerPresentationToUiMapper
import com.module.notelycompose.audio.ui.expect.PlatformAudioPlayer

class AudioPlayerModule {

    val platformAudioPlayer: PlatformAudioPlayer by lazy {
        PlatformAudioPlayer()
    }

    val audioPlayerPresentationToUiMapper: AudioPlayerPresentationToUiMapper by lazy {
        AudioPlayerPresentationToUiMapper()
    }
}
