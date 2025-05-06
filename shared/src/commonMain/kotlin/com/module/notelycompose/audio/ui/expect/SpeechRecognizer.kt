package com.module.notelycompose.audio.ui.expect

expect class SpeechRecognizer {
     fun initialize()
     fun finishRecognizer()
     fun stopRecognizer()
    fun startRecognizing(
        onComplete: (Boolean, String?) -> Unit
    )
    fun hasRecordingPermission(): Boolean
    suspend fun requestRecordingPermission():Boolean
}