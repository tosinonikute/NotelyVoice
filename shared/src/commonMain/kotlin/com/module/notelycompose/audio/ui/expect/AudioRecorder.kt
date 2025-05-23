package com.module.notelycompose.audio.ui.expect

expect class AudioRecorder {
    suspend fun setup()
    suspend fun teardown()
    fun startRecording()
    fun stopRecording()
    fun pauseRecording()
    fun resumeRecording()
    fun isPaused(): Boolean
    fun isRecording(): Boolean
    fun hasRecordingPermission(): Boolean
    fun getRecordingFilePath(): String
    suspend fun requestRecordingPermission(): Boolean
}
