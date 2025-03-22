package com.module.notelycompose.notes.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.audio.presentation.AudioPlayerUiState
import com.module.notelycompose.audio.ui.player.PlatformAudioPlayerUi
import com.module.notelycompose.audio.ui.recorder.RecordUiComponent
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.resources.vectors.IcRecorder
import com.module.notelycompose.resources.vectors.Images

@Composable
fun NoteDetailScreen(
    newNoteDateString: String,
    onNavigateBack: () -> Unit,
    editorState: EditorUiState,
    onUpdateContent: (newContent: TextFieldValue) -> Unit,
    onToggleBold: () -> Unit,
    onToggleItalic: () -> Unit,
    onToggleUnderline: () -> Unit,
    onSetAlignment: (alignment: TextAlign) -> Unit,
    onToggleBulletList: () -> Unit,
    onSelectTextSizeFormat: (textSize: Float) -> Unit
) {
    var showFormatBar by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var showRecordDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DetailNoteTopBar(
                onNavigateBack = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = LocalCustomColors.current.floatActionButtonBorderColor,
                        shape = CircleShape
                    ),
                backgroundColor = LocalCustomColors.current.bodyBackgroundColor,
                onClick = {
                    showRecordDialog = true
                }
            ) {
                Icon(
                    imageVector = Images.Icons.IcRecorder,
                    contentDescription = "Add",
                    tint = LocalCustomColors.current.bodyContentColor
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomNavigationBar(
                onToggleBold = { onToggleBold() },
                onToggleItalic = { onToggleItalic() },
                onToggleUnderline = { onToggleUnderline() },
                onSetAlignment = { alignment ->
                    onSetAlignment(alignment)
                },
                onToggleBulletList = { onToggleBulletList() },
                showFormatBar = showFormatBar,
                onShowTextFormatBar = { show ->
                    showFormatBar = show
                },
                onDeleteNote = {

                },
                onSelectTextSizeFormat = { textSize ->
                    onSelectTextSizeFormat(textSize)
                },
                selectionSize = editorState.selectionSize,
                textFieldFocusRequester = focusRequester
            )
        },
        content = { paddingValues ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .background(LocalCustomColors.current.bodyBackgroundColor)
                    .imePadding()
            ) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = newNoteDateString,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                        fontSize = 12.sp,
                        color = LocalCustomColors.current.bodyContentColor
                    )
                    PlatformAudioPlayerUi(
                        filePath = "",
                        uiState = AudioPlayerUiState(),
                        onLoadAudio = {},
                        onClear = {},
                        onSeekTo = {},
                        onTogglePlayPause = {}
                    )

                    val transformation = VisualTransformation { text ->
                        TransformedText(
                            buildAnnotatedString {
                                append(text)
                                editorState.formats.forEach { format ->
                                    addStyle(
                                        SpanStyle(
                                            fontWeight = if (format.isBold) FontWeight.Bold else null,
                                            fontStyle = if (format.isItalic) FontStyle.Italic else null,
                                            textDecoration = if (format.isUnderline)
                                                TextDecoration.Underline else null,
                                            fontSize = format.textSize?.sp ?: TextUnit.Unspecified
                                        ),
                                        format.range.first.coerceIn(0, text.length),
                                        format.range.last.coerceIn(0, text.length)
                                    )
                                }
                            },
                            OffsetMapping.Identity
                        )
                    }

                    BasicTextField(
                        value = editorState.content,
                        onValueChange = { onUpdateContent(it) },
                        modifier = Modifier
                            .fillMaxSize()
                            .height(600.dp)
                            .focusRequester(focusRequester)
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp),
                        textStyle = TextStyle(
                            color = LocalCustomColors.current.bodyContentColor,
                            textAlign = editorState.textAlign
                        ),
                        cursorBrush = SolidColor(LocalCustomColors.current.bodyContentColor),
                        readOnly = showFormatBar,
                        enabled = showRecordDialog.not(),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        visualTransformation = transformation
                    )

                }
            }
        }
    )

    // Component outside the scaffold to maintain full screen
    if (showRecordDialog) {
        RecordUiComponent(
            onDismiss = {
                showRecordDialog = false
            },
            onRecordingComplete = {
                showRecordDialog = false
                // Do something with recording
            },
            counterTimeString = "00:00"
        )
    }
}

