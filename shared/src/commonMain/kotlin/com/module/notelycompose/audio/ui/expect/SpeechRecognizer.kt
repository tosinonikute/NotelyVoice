package com.module.notelycompose.audio.ui.expect

expect class SpeechRecognizer {
     fun setup()
     fun tearDown()
     fun recognizeFile(filePath: String, onComplete:(String?)->Unit)
}