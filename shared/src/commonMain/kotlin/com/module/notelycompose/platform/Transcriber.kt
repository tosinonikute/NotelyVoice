package com.module.notelycompose.platform

expect class Transcriber {
    fun doesModelExists(modelFileName: String ): Boolean
    suspend fun initialize(modelFileName: String)
    suspend fun finish()
    suspend fun stop()
    suspend fun start(
        filePath:String, language:String,
        onProgress : (Int) -> Unit,
        onNewSegment : (Long, Long,String) -> Unit,
        onComplete : () -> Unit,
        onError : () -> Unit
    )
    fun hasRecordingPermission(): Boolean
    suspend fun requestRecordingPermission():Boolean
    fun isValidModel(modelFileName: String): Boolean
    suspend fun moveModelToProtectedStorage(modelFileName: String): Result<String>
    fun doesModelExistInExternalStorage(modelFileName: String): Boolean
    fun doesModelExistInInternalStorage(modelFileName: String): Boolean
}