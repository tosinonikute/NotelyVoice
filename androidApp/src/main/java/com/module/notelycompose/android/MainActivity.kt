package com.module.notelycompose.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.module.notelycompose.android.di.AudioRecorderModule
import com.module.notelycompose.android.presentation.AndroidAudioPlayerViewModel
import com.module.notelycompose.android.presentation.AndroidAudioRecorderViewModel
import com.module.notelycompose.android.presentation.AndroidNoteListViewModel
import com.module.notelycompose.android.presentation.AndroidTextEditorViewModel
import com.module.notelycompose.android.presentation.core.Routes
import com.module.notelycompose.android.presentation.ui.NoteListScreen
import com.module.notelycompose.notes.ui.detail.NoteDetailScreen
import com.module.notelycompose.notes.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var permissionLauncherHolder: AudioRecorderModule.PermissionLauncherHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeAudioRecorder()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NoteAppRoot()
                }
            }
        }
    }

    private fun initializeAudioRecorder() {
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }
        permissionLauncherHolder.permissionLauncher = permissionLauncher
    }

    override fun onDestroy() {
        super.onDestroy()
        permissionLauncherHolder.permissionLauncher = null
    }
}

@Composable
fun NoteAppRoot() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.LIST
    ) {
        composable(route = Routes.LIST) {
            val viewmodel = hiltViewModel<AndroidNoteListViewModel>()
            NoteListScreen(
                viewmodel = viewmodel,
                onFloatingActionButtonClicked = {
                    navController.navigate(Routes.DETAIL + "/0")
                },
                onNoteClicked = {
                    navController.navigate(Routes.DETAIL + "/$it")
                }
            )
        }

        composable(
            route = Routes.DETAIL + "/{noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId") ?: "0"

            val audioPlayerViewModel = hiltViewModel<AndroidAudioPlayerViewModel>()
            val audioPlayerPresentationState by audioPlayerViewModel.state.collectAsState()
            val audioPlayerState = audioPlayerViewModel.onGetUiState(audioPlayerPresentationState)

            val audioRecorderViewModel = hiltViewModel<AndroidAudioRecorderViewModel>()
            val audioRecorderPresentationState by audioRecorderViewModel.state.collectAsState()
            val audioRecorderState = audioRecorderViewModel.onGetUiState(audioRecorderPresentationState)

            val editorViewModel = hiltViewModel<AndroidTextEditorViewModel>()
            if(noteId.toLong() > 0L) editorViewModel.onGetNoteById(noteId)
            val editorPresentationState by editorViewModel.state.collectAsState()
            val editorState = editorViewModel.onGetUiState(editorPresentationState)
            val newNoteDateString = noteId.let { editorViewModel.getNewNoteContentDate(noteId) }

            NoteDetailScreen(
                newNoteDateString = newNoteDateString,
                onNavigateBack = {
                    navController.popBackStack()
                },
                editorState = editorState,
                onUpdateContent = { newContent ->
                    editorViewModel.onUpdateContent(newContent)
                },
                onToggleBulletList = { editorViewModel.onToggleBulletList() },
                onToggleBold = { editorViewModel.onToggleBold() },
                onToggleItalic = { editorViewModel.onToggleItalic() },
                onToggleUnderline = { editorViewModel.onToggleUnderline() },
                onSetAlignment = { alignment ->
                    editorViewModel.onSetAlignment(alignment)
                },
                onSelectTextSizeFormat = { textSize ->
                    editorViewModel.setTextSize(textSize)
                },
                onDeleteNote = {
                    editorViewModel.onDeleteNote()
                    navController.popBackStack()
                },

                onStartRecord = { audioRecorderViewModel.onStartRecording() },
                onStopRecord = { audioRecorderViewModel.onStopRecording() },
                onRequestAudioPermission = { audioRecorderViewModel.onRequestAudioPermission() },
                recordCounterString = audioRecorderState.recordCounterString,
                onAfterRecord = { editorViewModel.onUpdateRecordingPath(audioRecorderState.recordingPath) },

                onLoadAudio = { filePath -> audioPlayerViewModel.onLoadAudio(filePath) },
                onClear = { audioPlayerViewModel.onCleared() },
                onSeekTo = { position -> audioPlayerViewModel.onSeekTo(position) },
                onTogglePlayPause = { audioPlayerViewModel.onTogglePlayPause() },
                audioPlayerUiState = audioPlayerState,

                onStarNote = { editorViewModel.onToggleStar() }
            )
        }
    }
}
