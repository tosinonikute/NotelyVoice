package com.module.notelycompose.audio.ui.expect

actual class SpeechRecognitionManager {
    actual fun hasSpeechPermission(): Boolean {
        TODO("Not yet implemented")
    }





    actual fun startRecognition() {
    }

    actual fun stopRecognition() {
    }

    actual fun destroy() {
    }

    actual suspend fun requestPermissions(doOnGranted: () -> Unit) {
    }

    actual fun initializeRecognizer(
        onTextRecognized: (String, Boolean) -> Unit,
        onStatusChanged: (String) -> Unit,
        onError: (String) -> Unit,
        onSoundLevelChanged: (Float) -> Unit
    ) {
    }
}