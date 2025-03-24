package com.module.notelycompose

import com.module.notelycompose.audio.presentation.mappers.AudioRecorderPresentationToUiMapper
import com.module.notelycompose.audio.ui.expect.AudioRecorder

class AudioRecorderModule {

    val audioRecorder: AudioRecorder by lazy {
        AudioRecorder()
    }

    val audioRecorderPresentationToUiMapper: AudioRecorderPresentationToUiMapper by lazy {
        AudioRecorderPresentationToUiMapper()
    }
}
