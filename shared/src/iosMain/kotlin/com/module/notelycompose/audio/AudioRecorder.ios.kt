package com.module.notelycompose.audio

actual class AudioRecorder {
    actual fun startRecording() {
    }

    actual fun stopRecording() {
    }

    actual fun isRecording(): Boolean {
        TODO("Not yet implemented")
    }

    actual fun hasRecordingPermission(): Boolean {
        TODO("Not yet implemented")
    }

    actual suspend fun requestRecordingPermission() {
    }

    actual fun getRecordingFilePath(): String {
        TODO("Not yet implemented")
    }
}