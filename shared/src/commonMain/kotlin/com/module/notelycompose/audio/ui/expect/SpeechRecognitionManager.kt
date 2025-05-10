package com.module.notelycompose.audio.ui.expect

expect class SpeechRecognitionManager {
    fun hasSpeechPermission(): Boolean
    suspend fun requestPermissions(doOnGranted: () -> Unit)
    fun initializeRecognizer(
        onTextRecognized: (String, Boolean) -> Unit,
        onStatusChanged: (String) -> Unit,
        onError: (String) -> Unit,
        onSoundLevelChanged: (Float) -> Unit
    )
    fun startRecognition()
    fun stopRecognition()
    fun destroy()
}