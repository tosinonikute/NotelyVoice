package com.module.notelycompose.audio.ui.expect

import platform.AVFAudio.AVAudioPlayer
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import kotlinx.cinterop.*

private const val MILLISECONDS_MULTIPLIER = 1000
private const val SECONDS_DIVISOR = 1000.0
private const val DEFAULT_POSITION = 0.0
private const val ERROR_DURATION = 0

actual class PlatformAudioPlayer actual constructor() {
    private var audioPlayer: AVAudioPlayer? = null

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun prepare(filePath: String): Int {
        audioPlayer?.stop()

        return try {
            val fileManager = NSFileManager.defaultManager
            if (!fileManager.fileExistsAtPath(filePath)) {
                println("Error: File does not exist at path: $filePath")
                return ERROR_DURATION
            }
            val url = NSURL.fileURLWithPath(filePath)
            memScoped {
                val errorPtr: ObjCObjectVar<NSError?> = alloc()
                val player = AVAudioPlayer(contentsOfURL = url, error = errorPtr.ptr)

                if (player == null) {
                    val error = errorPtr.value
                    println("Error creating audio player: ${error?.localizedDescription ?: "Unknown error"}")
                    return ERROR_DURATION
                }

                audioPlayer = player
                (player.duration * MILLISECONDS_MULTIPLIER).toInt()
            }

        } catch (e: Exception) {
            println("Exception preparing audio player: ${e.message ?: "Unknown error"}")
            e.printStackTrace()
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
        audioPlayer?.setCurrentTime(DEFAULT_POSITION)
    }

    actual fun release() {
        audioPlayer?.stop()
        audioPlayer = null
    }

    actual fun seekTo(position: Int) {
        // Convert position from milliseconds to seconds
        val positionInSeconds = position / SECONDS_DIVISOR
        audioPlayer?.setCurrentTime(positionInSeconds)
    }

    actual fun getCurrentPosition(): Int {
        // Return position in milliseconds
        return ((audioPlayer?.currentTime ?: DEFAULT_POSITION) * SECONDS_DIVISOR).toInt()
    }

    actual fun isPlaying(): Boolean {
        return audioPlayer?.isPlaying() ?: false
    }
}
