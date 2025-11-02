package com.module.notelycompose

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher

class FileSaverLauncherHolder {
    var fileSaverLauncher: ActivityResultLauncher<String>? = null
    var onFileSaved: ((String) -> Unit)? = null
}

class FileSaverHandler(
    private val fileSaverLauncherHolder: FileSaverLauncherHolder
) {
    fun saveFile(defaultFileName: String, onFileSaved: (String) -> Unit) {
        fileSaverLauncherHolder.onFileSaved = onFileSaved
        fileSaverLauncherHolder.fileSaverLauncher?.launch(defaultFileName)
    }
}

class FolderPickerLauncherHolder {
    var folderPickerLauncher: ActivityResultLauncher<Uri?>? = null
    var onFolderSelected: ((Uri?) -> Unit)? = null
}

class FolderPickerHandler(
    private val folderPickerLauncherHolder: FolderPickerLauncherHolder
) {
    fun pickFolder(onFolderSelected: (Uri?) -> Unit) {
        folderPickerLauncherHolder.onFolderSelected = onFolderSelected
        folderPickerLauncherHolder.folderPickerLauncher?.launch(null)
    }
}
