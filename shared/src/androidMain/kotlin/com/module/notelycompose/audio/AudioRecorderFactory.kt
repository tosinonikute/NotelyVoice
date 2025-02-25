package com.module.notelycompose.audio

import android.content.Context
import androidx.activity.result.ActivityResultLauncher

object AudioRecorderFactory {
    fun create(
        context: Context,
        permissionLauncher: ActivityResultLauncher<String>?
    ): AudioRecorder {
        return AudioRecorder(context, permissionLauncher)
    }
}
