package audio

interface FileManager {
    fun launchAudioPicker(onResult: () -> Unit)
    fun launchVideoPicker(onResult: () -> Unit)

    suspend fun processPickedAudioToWav(onProgress: (Float) -> Unit): String?

    suspend fun processPickedVideoToWav(onProgress: (Float) -> Unit): String?
}