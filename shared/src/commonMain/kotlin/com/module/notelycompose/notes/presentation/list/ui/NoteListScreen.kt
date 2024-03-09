package com.module.notelycompose.notes.presentation.list.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.notes.domain.Note
import com.module.notelycompose.notes.presentation.list.NoteListUiState

@Composable
fun SharedNoteListScreen(
    noteListUiState: NoteListUiState,
    onFloatingActionButtonClicked: () -> Unit,
    onNoteClicked: (Int) -> Unit,
    onNoteDeleteClicked: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar()
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onFloatingActionButtonClicked()
                },
                backgroundColor = Color.Black
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
                    Text("Add Note", color = Color.White)
                    Spacer(modifier = Modifier.padding(4.dp))
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            WelcomeComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 16.dp
                    )
            )
            DateTabBar()
            NoteList(
                noteList = noteListUiState.notes,
                onNoteClicked = { id ->
                    onNoteClicked(id)
                },
                onNoteDeleteClicked = {
                    onNoteDeleteClicked(it)
                }
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteList(
    noteList: List<Note>,
    onNoteClicked: (Int) -> Unit,
    onNoteDeleteClicked: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(
            start = 20.dp,
            end = 20.dp,
        )
    ) {
        items(items = noteList) { note: Note ->
            NoteItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .animateItemPlacement(),
                note = note,
                onNoteClick = {
                    onNoteClicked(note.id)
                },
                onDeleteClick = {
                    onNoteDeleteClicked(note.id)
                }
            )
        }
    }
}

@Composable
fun WelcomeComponent(
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Black)
                .align(Alignment.CenterEnd),
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                tint = Color.White,
                modifier = Modifier.size(24.dp).align(Alignment.Center),
                contentDescription = ""
            )
        }

        Column(
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Text(
                text = "Welcome Back!",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                fontFamily = FontFamily.Default
            )
            Text(
                text = "Here's Your Notes Today.",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                fontFamily = FontFamily.Default
            )
        }

    }
}