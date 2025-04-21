package com.module.notelycompose.notes.ui.list

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
import com.module.notelycompose.notes.ui.list.model.NoteUiModel
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import notelycompose.shared.generated.resources.Res
import notelycompose.shared.generated.resources.note_list_add_note
import org.jetbrains.compose.resources.stringResource

@Composable
fun SharedNoteListScreen(
    notes: List<NoteUiModel>,
    onFloatingActionButtonClicked: () -> Unit,
    onNoteClicked: (Long) -> Unit,
    onNoteDeleteClicked: (NoteUiModel) -> Unit,
    onFilterTabItemClicked: (String) -> Unit,
    onSearchByKeyword: (String) -> Unit,
    selectedTabTitle: String
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
                        contentDescription = stringResource(Res.string.note_list_add_note),
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
            SearchBar(
                onSearchByKeyword = { keyword ->
                    onSearchByKeyword(keyword)
                }
            )
            FilterTabBar(
                selectedTabTitle = selectedTabTitle,
                onFilterTabItemClicked = { title ->
                    onFilterTabItemClicked(title)
                }
            )
            NoteList(
                noteList = notes,
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
