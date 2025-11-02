package audio.converter

interface AudioConverter {

    suspend fun convertAudioToWav(path: String, onProgress: (Float) -> Unit): String?
    suspend fun extractAudioFromVideoToWav(videoPath: String, onProgress: (Float) -> Unit): String?
}