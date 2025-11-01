package com.module.notelycompose.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import audio.recorder.AudioRecorder
import com.module.notelycompose.Arguments.NOTE_ID_PARAM
import com.module.notelycompose.Arguments.USE_BLUETOOTH_MIC
import com.module.notelycompose.audio.domain.SaveAudioNoteInteractor
import com.module.notelycompose.extensions.restartMainActivity
import com.module.notelycompose.onboarding.data.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.getValue

class AudioRecordingService : Service() {
    private val audioRecorder by inject<AudioRecorder>()
    private val saveAudioNoteInteractor by inject<SaveAudioNoteInteractor>()


    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var noteId: Long? = null
    private var useBluetoothMic = false

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, buildNotification())
        isRunning = true
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                noteId = intent.extras?.getLong(NOTE_ID_PARAM)
                useBluetoothMic = intent.extras?.getBoolean(USE_BLUETOOTH_MIC) ?: false
                startRecording()
            }
            ACTION_PAUSE -> pauseRecording()
            ACTION_RESUME -> resumeRecording()
            ACTION_STOP -> stopRecording()
            ACTION_STOP_FROM_TILE -> handleTileStop()
        }
        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "recording_channel",
            "Recording",
            NotificationManager.IMPORTANCE_LOW,
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun startRecording() {
        if (audioRecorder.hasRecordingPermission()) {
            audioRecorder.startRecording()
        }
    }

    private fun pauseRecording() {
        audioRecorder.pauseRecording()
    }

    private fun resumeRecording() {
        audioRecorder.resumeRecording()
    }

    private fun stopRecording() {
        audioRecorder.stopRecording()
        stopSelf()
    }

    private fun handleTileStop() {
        audioRecorder.stopRecording()
        coroutineScope.launch {
            saveAudioNoteInteractor.save(noteId)
            noteId = null
            stopSelf()
            this@AudioRecordingService.restartMainActivity()
        }
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, "recording_channel")
            .setContentTitle("Recording Note")
            .setContentText("Tap to stop recording")
            .setSmallIcon(android.R.drawable.presence_audio_online)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        var isRunning = false
        const val ACTION_START = "START_RECORDING"
        const val ACTION_PAUSE = "PAUSE_RECORDING"
        const val ACTION_RESUME = "RESUME_RECORDING"
        const val ACTION_STOP = "STOP_RECORDING"
        const val ACTION_STOP_FROM_TILE = "STOP_RECORDING_FROM_TILE"
    }
}
