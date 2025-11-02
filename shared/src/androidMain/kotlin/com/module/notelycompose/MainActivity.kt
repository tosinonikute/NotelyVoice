package com.module.notelycompose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import audio.utils.LauncherHolder
import org.koin.android.ext.android.inject
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.module.notelycompose.onboarding.data.PreferencesRepository
import com.module.notelycompose.platform.Theme
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private val fileSaverLauncherHolder by inject<FileSaverLauncherHolder>()
    private val folderPickerLauncherHolder by inject<FolderPickerLauncherHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        injectLauncher()
        setupFileSaverLauncher()
        setupFolderPickerLauncher()
        enableEdgeToEdge()
        setContent {
            val systemUiController = rememberSystemUiController()
            val preferenceRepository by inject<PreferencesRepository>()
            val uiMode by preferenceRepository.getTheme().collectAsState(Theme.SYSTEM.name)
            val darkTheme = when (uiMode) {
                Theme.DARK.name -> true
                Theme.LIGHT.name -> false
                else -> isSystemInDarkTheme()
            }
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = !darkTheme
            )
            App()
        }
    }

    private fun setupFileSaverLauncher() {
        fileSaverLauncherHolder.fileSaverLauncher =
            registerForActivityResult(ActivityResultContracts.CreateDocument("audio/wav")) { uri: Uri? ->
                uri?.let {
                    fileSaverLauncherHolder.onFileSaved?.invoke(uri.toString())
                }
            }
    }

    private fun setupFolderPickerLauncher() {
        folderPickerLauncherHolder.folderPickerLauncher =
            registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
                if (uri != null) {
                    contentResolver.takePersistableUriPermission(
                        uri,
                        android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                                android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
                // Always invoke callback, even if uri is null
                folderPickerLauncherHolder.onFolderSelected?.invoke(uri)
            }
    }

    private fun injectLauncher() {
        val launcherHolder by inject<LauncherHolder>()
        launcherHolder.init(this)
    }
}
