package com.module.notelycompose

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.uikit.OnFocusBehavior
import androidx.compose.ui.window.ComposeUIViewController
import com.module.notelycompose.notes.ui.detail.NoteActions
import com.module.notelycompose.notes.ui.detail.NoteAudioActions
import com.module.notelycompose.notes.ui.detail.NoteDetailScreen
import com.module.notelycompose.notes.ui.detail.NoteFormatActions
import com.module.notelycompose.notes.ui.detail.RecognitionActions
import com.module.notelycompose.notes.ui.theme.MyApplicationTheme

fun NoteDetailController(
    noteId: String? = null,
    onSaveClicked: () -> Unit,
    onNavigateBack: () -> Unit
) = ComposeUIViewController(
    configure = {
        onFocusBehavior = OnFocusBehavior.DoNothing
    }
) {
    MyApplicationTheme {

        val audioPlayerModule = AudioPlayerModule()
        val audioPlayerViewModel = remember {
            IOSAudioPlayerViewModel(
                audioPlayer = audioPlayerModule.platformAudioPlayer,
                mapper = audioPlayerModule.audioPlayerPresentationToUiMapper
            )
        }
        val audioPlayerPresentationState by audioPlayerViewModel.state.collectAsState()
        val audioPlayerState = audioPlayerViewModel.onGetUiState(audioPlayerPresentationState)

        val audioRecorderModule = AudioRecorderModule()
        val audioRecorderViewModel = remember {
            IOSAudioRecorderViewModel(
                audioRecorder = audioRecorderModule.audioRecorder,
                mapper = audioRecorderModule.audioRecorderPresentationToUiMapper
            )
        }


        val speechRecognitionModule = SpeechRecognitionModule()
        val speechRecognitionViewModel = remember {
            IOSSpeechRecognitionViewModel(
                speechRecognizer = speechRecognitionModule.speechRecognizer
            )
        }
        val speechRecognitionState by speechRecognitionViewModel.state.collectAsState()
        val audioRecorderPresentationState by audioRecorderViewModel.state.collectAsState()
        val audioRecorderState = audioRecorderViewModel.onGetUiState(audioRecorderPresentationState)

        val appModule = AppModule()
        val editorViewModel = remember {
            IOSTextEditorViewModel(
                getNoteByIdUseCase = appModule.getNoteById,
                deleteNoteUseCase = appModule.deleteNoteById,
                insertNoteUseCase = appModule.insertNote,
                updateNoteUseCase = appModule.updateNote,
                getLastNoteUseCase = appModule.getLastNoteUseCase,
                editorPresentationToUiStateMapper = appModule.editorPresentationToUiStateMapper,
                textFormatPresentationMapper = appModule.textFormatPresentationMapper,
                textAlignPresentationMapper = appModule.textAlignPresentationMapper,
                textEditorHelper = appModule.textEditorHelper
            )
        }
        if (noteId != null) editorViewModel.onGetNoteById(noteId)
        val editorPresentationState by editorViewModel.state.collectAsState()
        val editorState = editorViewModel.onGetUiState(editorPresentationState)

        val formatActions = NoteFormatActions(
            onToggleBold = editorViewModel::onToggleBold,
            onToggleItalic = editorViewModel::onToggleItalic,
            onToggleUnderline = editorViewModel::onToggleUnderline,
            onSetAlignment = editorViewModel::onSetAlignment,
            onToggleBulletList = editorViewModel::onToggleBulletList,
            onSelectTextSizeFormat = editorViewModel::setTextSize
        )

        val audioActions = NoteAudioActions(
            onStartRecord = audioRecorderViewModel::onStartRecording,
            onStopRecord = audioRecorderViewModel::onStopRecording,
            onRequestAudioPermission = audioRecorderViewModel::onRequestAudioPermission,
            onAfterRecord = {editorViewModel.onUpdateRecordingPath(audioRecorderState.recordingPath) },
            onDeleteRecord = {editorViewModel.onDeleteRecord()},
            onLoadAudio = audioPlayerViewModel::onLoadAudio,
            onClear = audioPlayerViewModel::onCleared,
            onSeekTo = audioPlayerViewModel::onSeekTo,
            onTogglePlayPause = audioPlayerViewModel::onTogglePlayPause,
            setupRecorder = audioRecorderViewModel::setupRecorder,
            finishRecorder = audioRecorderViewModel::finishRecorder

        )

        val recognitionActions = RecognitionActions(
            recognizeAudio = speechRecognitionViewModel::onStartRecognizing,
            stopRecognition = speechRecognitionViewModel::finishRecognizer,
            summarize = speechRecognitionViewModel::summarize
        )

        val noteActions = NoteActions(
            onDeleteNote = {
                editorViewModel.onDeleteNote()
                onNavigateBack()
            },
            onStarNote = editorViewModel::onToggleStar
        )

        NoteDetailScreen(
            newNoteDateString = editorState.createdAt,
            editorState = editorState,
            audioPlayerUiState = audioPlayerState,
            recordCounterString = audioRecorderState.recordCounterString,
            onNavigateBack = onNavigateBack,
            onUpdateContent = editorViewModel::onUpdateContent,
            onFormatActions = formatActions,
            onAudioActions = audioActions,
            onNoteActions = noteActions,
            onRecognitionActions = recognitionActions,
            transcriptionUiState = speechRecognitionState
        )
    }
}
