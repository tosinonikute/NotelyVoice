package com.module.notelycompose.audio.ui.expect

actual class PlatformAudioPlayer {
    private var mediaPlayer: android.media.MediaPlayer? = null

    actual suspend fun prepare(filePath: String): Int {
        mediaPlayer?.release()
        try {
            val player = android.media.MediaPlayer().apply {
                setDataSource(filePath)
                prepare()
            }
            mediaPlayer = player
            return player.duration
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
    }

    actual fun play() {
        mediaPlayer?.start()
    }

    actual fun pause() {
        mediaPlayer?.pause()
    }

    actual fun stop() {
        mediaPlayer?.stop()
    }

    actual fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    actual fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    actual fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    actual fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }
}