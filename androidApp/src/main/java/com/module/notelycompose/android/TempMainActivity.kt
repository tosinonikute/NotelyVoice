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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.module.notelycompose.android.presentation.AndroidNoteDetailViewModel
import com.module.notelycompose.android.presentation.AndroidNoteListViewModel
import com.module.notelycompose.android.presentation.core.Routes
import com.module.notelycompose.notes.domain.Note
import com.module.notelycompose.notes.presentation.detail.NoteDetailScreenEvent
import com.module.notelycompose.notes.presentation.detail.userinterface.NoteDetailScreen
import com.module.notelycompose.notes.presentation.list.NoteListEvent
import com.module.notelycompose.notes.presentation.list.ui.SharedNoteListScreen
import com.module.notelycompose.notes.presentation.list.userinterface.SharedNoteListScreen2
import com.module.notelycompose.notes.presentation.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TempMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NoteAppRoot2()
                }
            }
        }
    }
}

@Composable
fun NoteAppRoot2() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.LIST
    ) {
        composable(route = Routes.LIST) {
            val viewmodel = hiltViewModel<AndroidNoteListViewModel>()
            NoteListScreen2(
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
            val viewmodel = hiltViewModel<AndroidNoteDetailViewModel>()
            val note: Note? = viewmodel.getNoteById(noteId)
            val newNoteDateString = noteId.let { viewmodel.getNewNoteContentDate(noteId) }

            NoteDetailScreen(
                title = note?.title,
                content = note?.content,
                newNoteDateString = newNoteDateString,
                onSaveClick = { title, content, isUpdate ->
                    if (isUpdate) {
                        viewmodel.onEvent(NoteDetailScreenEvent.DeleteNote(noteId))
                    }
                    viewmodel.onEvent(
                        NoteDetailScreenEvent.NoteSaved(
                            title, content
                        )
                    )
                    navController.navigate(Routes.LIST)
                }
            )
        }
    }
}

@Composable
fun NoteListScreen2(
    viewmodel: AndroidNoteListViewModel,
    onFloatingActionButtonClicked: () -> Unit,
    onNoteClicked: (Int) -> Unit
) {
    val state by viewmodel.state.collectAsState()

    SharedNoteListScreen2(
        noteListUiState = state,
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
