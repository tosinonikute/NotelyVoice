package com.module.notelycompose.notes.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.module.notelycompose.notes.ui.list.model.NoteUiModel
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch
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
    val coroutineScope = rememberCoroutineScope()
    var isSettingsTapped by remember { mutableStateOf(false) }

    // State to control bottom sheet
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = false
    )

    // State to toggle full screen mode
    var isFullScreen by remember { mutableStateOf(true) }

    // Function to handle bottom sheet dismissal
    val dismissBottomSheet: () -> Unit = {
        coroutineScope.launch {
            bottomSheetState.hide()
        }
    }

    // Function to toggle full screen mode
    val toggleFullScreen: () -> Unit = {
        isFullScreen = !isFullScreen
    }

    val navigateToWebPage: (String, String) -> Unit = { title, url ->
        // This function handles navigation to web pages
        // The actual navigation is handled inside the SettingsBottomSheet
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            if(isSettingsTapped) {
                SettingsBottomSheet(
                    onDismiss = dismissBottomSheet
                )
            } else {
                InfoBottomSheet(
                    onDismiss = dismissBottomSheet,
                    onNavigateToWebPage = navigateToWebPage
                )
            }
        },
        sheetElevation = 8.dp,
        sheetBackgroundColor = LocalCustomColors.current.backgroundViewColor,
        // Full screen option
        sheetContentColor = LocalCustomColors.current.bodyContentColor,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        modifier = if (isFullScreen && bottomSheetState.isVisible) {
            Modifier.fillMaxSize()
        } else {
            Modifier
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    onMenuClicked = {
                        isSettingsTapped = false
                        coroutineScope.launch {
                            if (bottomSheetState.isVisible) {
                                bottomSheetState.hide()
                            } else {
                                bottomSheetState.show()
                            }
                        }
                    },
                    onSettingsClicked = {
                        isSettingsTapped = true
                        coroutineScope.launch {
                            if (bottomSheetState.isVisible) {
                                bottomSheetState.hide()
                            } else {
                                bottomSheetState.show()
                            }
                        }
                    }
                )
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
                            // Additionally, consider hiding the bottom sheet when tapping elsewhere
                            if (bottomSheetState.isVisible) {
                                coroutineScope.launch {
                                    bottomSheetState.hide()
                                }
                            }
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
}
