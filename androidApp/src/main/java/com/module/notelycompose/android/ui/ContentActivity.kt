package com.module.notelycompose.android.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.module.notelycompose.android.theme.LocalCustomColors
import com.module.notelycompose.android.theme.MyApplicationTheme
import com.module.notelycompose.notes.domain.model.Note
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NoteAppUi()
                    // ContentBottomAppBar()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteAppUi() {
    NoteScreen()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteScreen() {
    val focusManager = LocalFocusManager.current

    val note = Note(
        id = 1,
        title = "Graduation Cap SVG Vector",
        content = "Free Download Graduation Cap 13 SVG vector file in monocolor and multicolor type for Sketch and Figma from Graduation Cap 13 Vectors svg vector collection. Graduation Cap 13 Vectors SVG",
        colorHex = Note.generateRandomColor()
    )
    Scaffold(
        topBar = {
            ContentTopBar()
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                },
                backgroundColor = LocalCustomColors.current.backgroundViewColor
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Icon(
                        modifier = Modifier.padding(4.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Note",
                        tint = LocalCustomColors.current.floatActionButtonIconColor
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(LocalCustomColors.current.bodyBackgroundColor)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
        ) {
            ContentSearchBar()
            ContentDateTabBar()
            ContentNoteList(
                noteList = listOf(note, note, note, note, note, note, note, note),
                onNoteClicked = {
                },
                onNoteDeleteClicked = {

                }
            )
        }
    }
}
