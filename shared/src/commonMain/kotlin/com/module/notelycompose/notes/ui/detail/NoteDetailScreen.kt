package com.module.notelycompose.notes.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.module.notelycompose.audio.ui.player.PlatformAudioPlayerUi
import com.module.notelycompose.audio.ui.player.model.AudioPlayerUiState
import com.module.notelycompose.audio.ui.recorder.RecordUiComponent
import com.module.notelycompose.notes.ui.extensions.showKeyboard
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.resources.vectors.IcRecorder
import com.module.notelycompose.resources.vectors.Images
import com.module.notelycompose.transcription.TranscriptionDialog
import com.module.notelycompose.transcription.TranscriptionUiState
import kotlinx.coroutines.launch
import notelycompose.shared.generated.resources.Res
import notelycompose.shared.generated.resources.ic_transcription
import notelycompose.shared.generated.resources.note_detail_recorder
import notelycompose.shared.generated.resources.transcription_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NoteDetailScreen(
    newNoteDateString: String,
    editorState: EditorUiState,
    audioPlayerUiState: AudioPlayerUiState,
    transcriptionUiState: TranscriptionUiState,
    recordCounterString: String,
    onNavigateBack: () -> Unit,
    onUpdateContent: (newContent: TextFieldValue) -> Unit,
    onFormatActions: NoteFormatActions,
    onAudioActions: NoteAudioActions,
    onRecognitionActions: RecognitionActions,
    onNoteActions: NoteActions
) {
    var showFormatBar by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var showRecordDialog by remember { mutableStateOf(false) }
    var showTranscriptionDialog by remember { mutableStateOf(false) }
    var isTextFieldFocused by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
// Setup when dialog appears
    DisposableEffect(Unit) {
        val job = coroutineScope.launch {
            onAudioActions.setupRecorder()
        }
        onDispose {
            coroutineScope.launch {
                job.cancel()
                onAudioActions.finishRecorder()
            }
        }
    }
    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = { DetailNoteTopBar(onNavigateBack = onNavigateBack) },
        floatingActionButton = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    FloatingActionButton(
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = LocalCustomColors.current.floatActionButtonBorderColor,
                            shape = CircleShape
                        ),
                        backgroundColor = LocalCustomColors.current.bodyBackgroundColor,
                        onClick = { showTranscriptionDialog = true }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_transcription),
                            contentDescription = stringResource(Res.string.transcription_icon),
                            tint = LocalCustomColors.current.bodyContentColor
                        )
                    }

                FloatingActionButton(
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = LocalCustomColors.current.floatActionButtonBorderColor,
                        shape = CircleShape
                    ),
                    backgroundColor = LocalCustomColors.current.bodyBackgroundColor,
                    onClick = { showRecordDialog = true }
                ) {
                    Icon(
                        imageVector = Images.Icons.IcRecorder,
                        contentDescription = stringResource(Res.string.note_detail_recorder),
                        tint = LocalCustomColors.current.bodyContentColor
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomNavigationBar(
                isTextFieldFocused = isTextFieldFocused,
                selectionSize = editorState.selectionSize,
                isStarred = editorState.isStarred,
                showFormatBar = showFormatBar,
                textFieldFocusRequester = focusRequester,
                onFormatActions = onFormatActions,
                onShowTextFormatBar = { showFormatBar = it },
                onStarNote = onNoteActions.onStarNote,
                onDeleteNote = onNoteActions.onDeleteNote
            )
        }
    ) { paddingValues ->

            NoteContent(
                paddingValues = paddingValues,
                newNoteDateString = newNoteDateString,
                editorState = editorState,
                showFormatBar = showFormatBar,
                showRecordDialog = showRecordDialog,
                focusRequester = focusRequester,
                onUpdateContent = onUpdateContent,
                audioPlayerUiState = audioPlayerUiState,
                onAudioActions = onAudioActions,
                onFocusChange = {
                    isTextFieldFocused = it
                },

                )
    }

    if (showRecordDialog) {
        RecordUiComponent(
            onDismiss = { showRecordDialog = false },
            onAfterRecord = {
                showRecordDialog = false
                onAudioActions.onAfterRecord()
            },
            recordCounterString = recordCounterString,
            onStartRecord = onAudioActions.onStartRecord,
            onStopRecord = onAudioActions.onStopRecord,
        )
    }

    if (showTranscriptionDialog) {
        LocalSoftwareKeyboardController.current?.hide()
        TranscriptionDialog(
            modifier = Modifier.fillMaxSize(),
            transcriptionUiState,
            onAskingForAudioPermission = { onRecognitionActions.requestAudioPermission() },
            onRecognitionInitialized = { onRecognitionActions.initRecognizer() },
            onRecognitionFinished = { onRecognitionActions.finishRecognizer() },
            onRecognitionStart = { onRecognitionActions.startRecognizer() },
            onRecognitionStopped = {onRecognitionActions.stopRecognition()},
            onDismiss = { showTranscriptionDialog = false },
            onSummarizeContent = {
               onRecognitionActions.summarize()
            },
            onAppendContent = {
                onUpdateContent(TextFieldValue("${editorState.content.text}\n$it"))
                showTranscriptionDialog = false
            }
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun NoteContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    newNoteDateString: String,
    editorState: EditorUiState,
    showFormatBar: Boolean,
    showRecordDialog: Boolean,
    focusRequester: FocusRequester,
    onFocusChange:(Boolean)->Unit,
    onUpdateContent: (TextFieldValue) -> Unit,
    audioPlayerUiState: AudioPlayerUiState,
    onAudioActions: NoteAudioActions
) {
    val scrollState = rememberScrollState()
    LaunchedEffect(editorState.content) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(scrollState)
            .background(LocalCustomColors.current.bodyBackgroundColor)
            .imePadding()
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            DateHeader(newNoteDateString)

            if (editorState.recording.isRecordingExist) {
                val dismissState = rememberDismissState()

                if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                    LaunchedEffect(Unit) {
                        onAudioActions.onDeleteRecord()
                    }
                }

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    background = {
                        // Background that appears when swiping
                        Box(
                            modifier = Modifier
                                .width(800.dp)
                                .height(36.dp)
                                .padding(horizontal = 16.dp, vertical = 0.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Red),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                        }
                    },
                    dismissContent = {
                        PlatformAudioPlayerUi(
                            filePath = editorState.recording.recordingPath,
                            uiState = audioPlayerUiState,
                            onLoadAudio = onAudioActions.onLoadAudio,
                            onClear = onAudioActions.onClear,
                            onSeekTo = onAudioActions.onSeekTo,
                            onTogglePlayPause = onAudioActions.onTogglePlayPause
                        )
                    }
                )
            }

            NoteEditor(
                modifier= Modifier.fillMaxWidth().weight(1f),
                editorState = editorState,
                showFormatBar = showFormatBar,
                showRecordDialog = showRecordDialog,
                focusRequester = focusRequester,
                onFocusChange = onFocusChange,
                onUpdateContent = onUpdateContent,
            )
        }
    }
}


@Composable
private fun DateHeader(dateString: String) {
    Text(
        text = dateString,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        fontSize = 12.sp,
        color = LocalCustomColors.current.bodyContentColor
    )
}

@Composable
private fun NoteEditor(
    modifier: Modifier = Modifier,
    editorState: EditorUiState,
    showFormatBar: Boolean,
    showRecordDialog: Boolean,
    focusRequester: FocusRequester,
    onFocusChange:(Boolean)->Unit,
    onUpdateContent: (TextFieldValue) -> Unit
) {

//    val scrollState = rememberScrollState()
//    LaunchedEffect(editorState.content) {
//        scrollState.animateScrollTo(scrollState.maxValue)
//    }
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
        onValueChange = onUpdateContent,
        modifier =
            modifier
            .focusRequester(focusRequester)
            .padding(horizontal = 16.dp)
            .onFocusChanged {
                onFocusChange(it.isFocused)
            },
        textStyle = TextStyle(
            color = LocalCustomColors.current.bodyContentColor,
            textAlign = editorState.textAlign
        ),
        cursorBrush = SolidColor(LocalCustomColors.current.bodyContentColor),
        readOnly = showFormatBar,
        enabled = !showRecordDialog,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences
        ),
        visualTransformation = transformation
    )
}

// Helper data classes to group related callbacks
data class NoteFormatActions(
    val onToggleBold: () -> Unit,
    val onToggleItalic: () -> Unit,
    val onToggleUnderline: () -> Unit,
    val onSetAlignment: (TextAlign) -> Unit,
    val onToggleBulletList: () -> Unit,
    val onSelectTextSizeFormat: (Float) -> Unit
)

data class NoteAudioActions(
    val onStartRecord: (()->Unit) -> Unit,
    val onStopRecord: () -> Unit,
    val setupRecorder: suspend () -> Unit,
    val finishRecorder: suspend () -> Unit,
    val onRequestAudioPermission: () -> Unit,
    val onAfterRecord: () -> Unit,
    val onDeleteRecord: () -> Unit,
    val onLoadAudio: (String) -> Unit,
    val onClear: () -> Unit,
    val onSeekTo: (Int) -> Unit,
    val onTogglePlayPause: () -> Unit
)

data class RecognitionActions(
    val requestAudioPermission: () -> Unit,
    val initRecognizer: () -> Unit,
    val finishRecognizer: () -> Unit,
    val startRecognizer: () -> Unit,
    val stopRecognition: () -> Unit,
    val summarize: () -> Unit
)

data class NoteActions(
    val onDeleteNote: () -> Unit,
    val onStarNote: () -> Unit
)

