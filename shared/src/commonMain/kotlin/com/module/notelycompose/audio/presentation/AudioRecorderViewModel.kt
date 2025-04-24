package com.module.notelycompose.audio.presentation

import com.module.notelycompose.audio.presentation.mappers.AudioRecorderPresentationToUiMapper
import com.module.notelycompose.audio.ui.expect.AudioRecorder
import com.module.notelycompose.audio.ui.recorder.AudioRecorderUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

private const val RECORD_COUNTER_START = "00:00"
private const val SECONDS_IN_MINUTE = 60
private const val LEADING_ZERO_THRESHOLD = 10
private const val INITIAL_SECOND = 0

data class AudioRecorderPresentationState(
    val recordCounterString: String = RECORD_COUNTER_START,
    val recordingPath: String = ""
)

class AudioRecorderViewModel(
    private val audioRecorder: AudioRecorder,
    private val mapper: AudioRecorderPresentationToUiMapper,
    coroutineScope: CoroutineScope? = null
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _audioRecorderPresentationState = MutableStateFlow(AudioRecorderPresentationState())
    val audioRecorderPresentationState: StateFlow<AudioRecorderPresentationState> = _audioRecorderPresentationState

    private var counterJob: Job? = null
    private var recordingTimeSeconds = INITIAL_SECOND

    fun onStartRecording(updateUI:()->Unit) {
        viewModelScope.launch {
            if (!audioRecorder.hasRecordingPermission()) {
             audioRecorder.requestRecordingPermission()
                if (!audioRecorder.hasRecordingPermission()) {
                    return@launch
                }
            }

            if (!audioRecorder.isRecording()) {
                audioRecorder.startRecording()
                updateUI()
                startCounter()
            }
        }
    }



    private fun startCounter() {
        // Reset counter
        recordingTimeSeconds = INITIAL_SECOND
        _audioRecorderPresentationState.value = _audioRecorderPresentationState.value.copy(
            recordCounterString = RECORD_COUNTER_START
        )

        counterJob?.cancel()
        counterJob = viewModelScope.launch {
            while (true) {
                delay(1.seconds)
                recordingTimeSeconds++
                updateCounterString()
            }
        }
    }

    private fun updateCounterString() {
        val minutes = recordingTimeSeconds / SECONDS_IN_MINUTE
        val seconds = recordingTimeSeconds % SECONDS_IN_MINUTE
        val minutesStr = if (minutes < LEADING_ZERO_THRESHOLD) "0$minutes" else "$minutes"
        val secondsStr = if (seconds < LEADING_ZERO_THRESHOLD) "0$seconds" else "$seconds"
        val counterString = "$minutesStr:$secondsStr"

        _audioRecorderPresentationState.update { current ->
            current.copy(recordCounterString = counterString)
        }
    }

    fun onStopRecording() {
        if (audioRecorder.isRecording()) {
            audioRecorder.stopRecording()
            val recordingPath = audioRecorder.getRecordingFilePath()
            stopCounter()

            _audioRecorderPresentationState.update { current ->
                current.copy(recordingPath = recordingPath)
            }
        }
    }

    suspend fun setupRecorder(){
            audioRecorder.setup()
    }
    suspend fun finishRecorder(){
            audioRecorder.teardown()

    }

    private fun stopCounter() {
        counterJob?.cancel()
        counterJob = null
    }

    fun onCleared() {
        stopCounter()
        if (audioRecorder.isRecording()) {
            audioRecorder.stopRecording()
        }
    }

    fun onRequestAudioPermission() {
        viewModelScope.launch {
            if (!audioRecorder.hasRecordingPermission()) {
                audioRecorder.requestRecordingPermission()
                if (!audioRecorder.hasRecordingPermission()) {
                    return@launch
                }
            }
        }
    }

    fun onGetUiState(presentationState: AudioRecorderPresentationState): AudioRecorderUiState {
        return mapper.mapToUiState(presentationState)
    }
}
