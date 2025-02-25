package com.module.notelycompose.audio

actual class PlatformAudioPlayer actual constructor() {
    actual suspend fun prepare(filePath: String): Int {
        TODO("Not yet implemented")
    }

    actual fun play() {
    }

    actual fun pause() {
    }

    actual fun stop() {
    }

    actual fun release() {
    }

    actual fun seekTo(position: Int) {
    }

    actual fun getCurrentPosition(): Int {
        TODO("Not yet implemented")
    }

    actual fun isPlaying(): Boolean {
        TODO("Not yet implemented")
    }
}