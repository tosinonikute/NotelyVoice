package com.module.notelycompose.notes.presentation.list.userinterface

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
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.module.notelycompose.notes.presentation.list.NoteListUiState
import com.module.notelycompose.notes.presentation.theme.LocalCustomColors

@Composable
fun SharedNoteListScreen2(
    noteListUiState: NoteListUiState,
    onFloatingActionButtonClicked: () -> Unit,
    onNoteClicked: (Int) -> Unit,
    onNoteDeleteClicked: (Int) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopBar()
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onFloatingActionButtonClicked()
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
            SearchBar()
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
