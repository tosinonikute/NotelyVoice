package com.module.notelycompose

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.uikit.OnFocusBehavior
import androidx.compose.ui.window.ComposeUIViewController
import com.module.notelycompose.notes.ui.detail.NoteDetailScreen
import com.module.notelycompose.notes.presentation.list.NoteListEvent
import com.module.notelycompose.notes.ui.list.SharedNoteListScreen
import com.module.notelycompose.notes.ui.theme.MyApplicationTheme
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(DelicateCoroutinesApi::class)
fun NoteListController(
    onFloatingActionButtonClicked: () -> Unit,
    onNoteClicked: (Long) -> Unit,
) = ComposeUIViewController {
    MyApplicationTheme {
        val appModule = remember { AppModule() }
        val viewmodel = remember {
            IOSNoteListViewModel(
                getAllNotesUseCase = appModule.getAllNotesUseCase,
                insertNoteUseCase = appModule.insertNote,
                deleteNoteById = appModule.deleteNoteById,
                noteUiMapper = appModule.noteUiMapper
            )
        }
        val state = viewmodel.state.collectAsState()
        SharedNoteListScreen(
            notes = state.value.notes,
            onFloatingActionButtonClicked = onFloatingActionButtonClicked,
            onNoteClicked = onNoteClicked,
            onNoteDeleteClicked = { id ->
                viewmodel.onEvent(NoteListEvent.OnNoteDeleted(id))
            }
        )
    }
}

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
                textAlignPresentationMapper = appModule.textAlignPresentationMapper
            )
        }
        if (noteId != null) editorViewModel.onGetNoteById(noteId)
        val editorPresentationState by editorViewModel.state.collectAsState()
        val editorState = editorViewModel.onGetUiState(editorPresentationState)
        val newNoteDateString = noteId?.let { editorViewModel.getNewNoteContentDate(noteId) } ?: ""

        NoteDetailScreen(
            newNoteDateString = newNoteDateString,
            onNavigateBack = {
                onNavigateBack()
            },
            editorState = editorState,
            onUpdateContent = { newContent ->
                editorViewModel.onUpdateContent(newContent)
                onSaveClicked()
            },
            onToggleBulletList = { editorViewModel.onToggleBulletList() },
            onToggleBold = { editorViewModel.onToggleBold() },
            onToggleItalic = { editorViewModel.onToggleItalic() },
            onToggleUnderline = { editorViewModel.onToggleUnderline() },
            onSetAlignment = { alignment ->
                editorViewModel.onSetAlignment(alignment)
            },
            onSelectTextSizeFormat = { textSize ->
                editorViewModel.setTextSize(textSize)
            },
            onDeleteNote = {
                editorViewModel.onDeleteNote()
                onNavigateBack()
            },

            onStartRecord = { audioRecorderViewModel.onStartRecording() },
            onStopRecord = { audioRecorderViewModel.onStopRecording() },
            onRequestAudioPermission = { audioRecorderViewModel.onRequestAudioPermission() },
            recordCounterString = audioRecorderState.recordCounterString,
            onAfterRecord = { editorViewModel.onUpdateRecordingPath(audioRecorderState.recordingPath) },

            onLoadAudio = { filePath -> audioPlayerViewModel.onLoadAudio(filePath) },
            onClear = { audioPlayerViewModel.onCleared() },
            onSeekTo = { position -> audioPlayerViewModel.onSeekTo(position) },
            onTogglePlayPause = { audioPlayerViewModel.onTogglePlayPause() },
            audioPlayerUiState = audioPlayerState,

            onStarNote = { editorViewModel.onToggleStar() }
        )
    }
}
