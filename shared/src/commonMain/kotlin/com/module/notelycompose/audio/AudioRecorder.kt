package com.module.notelycompose.audio

expect class AudioRecorder {
    fun startRecording()
    fun stopRecording()
    fun isRecording(): Boolean
    fun hasRecordingPermission(): Boolean
    suspend fun requestRecordingPermission()
    fun getRecordingFilePath(): String
}
