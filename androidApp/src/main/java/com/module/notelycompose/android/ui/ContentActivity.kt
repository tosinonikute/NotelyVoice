package com.module.notelycompose.android.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.android.R
import com.module.notelycompose.android.theme.LocalCustomColors
import com.module.notelycompose.android.theme.MyApplicationTheme
import com.module.notelycompose.core.DateTimeUtil
import com.module.notelycompose.notes.domain.Note
import com.module.notelycompose.notes.presentation.list.ui.NoteList
import dagger.hilt.android.AndroidEntryPoint
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.TimeZone

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
                        tint = Color.White
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
                iconList = listOf(getRandomIcon(), getRandomIcon(), getRandomIcon(), getRandomIcon(), getRandomIcon(), getRandomIcon(), getRandomIcon(), getRandomIcon()),
                onNoteClicked = {
                },
                onNoteDeleteClicked = {

                }
            )
        }
    }
}

@Composable
private fun getRandomIcon(): Painter = listOf(
    painterResource(id = R.drawable.ic_list_ballon),
    painterResource(id = R.drawable.ic_list_disk),
    painterResource(id = R.drawable.ic_list_football),
    painterResource(id = R.drawable.ic_list_graduation),
    painterResource(id = R.drawable.ic_list_bald_bomb),
    painterResource(id = R.drawable.ic_list_bald_leaf),
    painterResource(id = R.drawable.ic_list_bald_heart)
).random()