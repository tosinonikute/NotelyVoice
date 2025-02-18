package com.module.notelycompose.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.module.notelycompose.android.presentation.AndroidNoteListViewModel
import com.module.notelycompose.android.presentation.AndroidTextEditorViewModel
import com.module.notelycompose.android.presentation.core.Routes
import com.module.notelycompose.notes.presentation.detail.userinterface.NoteDetailScreen
import com.module.notelycompose.notes.presentation.list.NoteListEvent
import com.module.notelycompose.notes.presentation.list.userinterface.SharedNoteListScreen
import com.module.notelycompose.notes.presentation.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                }
            )
        }
    }
}

@Composable
fun NoteListScreen(
    viewmodel: AndroidNoteListViewModel,
    onFloatingActionButtonClicked: () -> Unit,
    onNoteClicked: (Long) -> Unit
) {
    val state by viewmodel.state.collectAsState()

    SharedNoteListScreen(
        notes = state.notes,
        onFloatingActionButtonClicked = {
            onFloatingActionButtonClicked()
        },
        onNoteClicked = {
            onNoteClicked(it)
        },
        onNoteDeleteClicked = {
            viewmodel.onEvent(NoteListEvent.OnNoteDeleted(it))
        }
    )
}

@Composable
fun HelloWorldViewForUiTest2(text: String) {
    Text(text = text)
}
