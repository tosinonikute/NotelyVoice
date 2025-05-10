package com.module.notelycompose

import com.module.notelycompose.audio.presentation.mappers.AudioRecorderPresentationToUiMapper
import com.module.notelycompose.audio.ui.expect.AudioRecorder
import com.module.notelycompose.audio.ui.expect.SpeechRecognizer

class SpeechRecognitionModule {

    val speechRecognizer: SpeechRecognizer by lazy {
        SpeechRecognizer()
    }
}
