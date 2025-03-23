package com.module.notelycompose.audio.ui.expect

import kotlinx.cinterop.*
import platform.AVFAudio.AVAudioPlayer
import platform.Foundation.*

actual class PlatformAudioPlayer actual constructor() {
    private var audioPlayer: AVAudioPlayer? = null

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun prepare(filePath: String): Int {
        audioPlayer?.stop()

        return try {
            val url = NSURL.fileURLWithPath(filePath)
            val player = AVAudioPlayer(contentsOfURL = url, error = null)
            audioPlayer = player

            // Convert duration to milliseconds (AVAudioPlayer uses seconds)
            (player.duration * 1000).toInt()
        } catch (e: Exception) {
            println("Error preparing audio player: ${e.message}")
            0
        }
    }

    actual fun play() {
        audioPlayer?.play()
    }

    actual fun pause() {
        audioPlayer?.pause()
    }

    actual fun stop() {
        audioPlayer?.stop()
        audioPlayer?.setCurrentTime(0.0)
    }

    actual fun release() {
        audioPlayer?.stop()
        audioPlayer = null
    }

    actual fun seekTo(position: Int) {
        // Convert position from milliseconds to seconds
        val positionInSeconds = position / 1000.0
        audioPlayer?.setCurrentTime(positionInSeconds)
    }

    actual fun getCurrentPosition(): Int {
        // Return position in milliseconds
        return ((audioPlayer?.currentTime ?: 0.0) * 1000.0).toInt()
    }

    actual fun isPlaying(): Boolean {
        return audioPlayer?.isPlaying() ?: false
    }
}
