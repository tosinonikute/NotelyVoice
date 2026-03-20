# Move Model to Protected Storage - Implementation Guide

## Overview

This document describes the implementation of the "Move Model to Protected Storage" feature, which prevents voice models from being automatically deleted by aggressive OS storage management on custom Android ROMs (GrapheneOS, MIUI, etc.).

## Problem Statement

### Original Issue
Voice models (~500MB) were being repeatedly re-downloaded on custom Android OSes because:
1. Models stored in `getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)` were being cleared by aggressive OS storage management
2. The app had no way to move models to protected internal storage
3. File copy operations were blocking the main thread, causing UI freezes and crashes

### Root Cause
- External files directory is subject to OS cleanup policies
- Custom Android ROMs (GrapheneOS, MIUI) have aggressive storage management
- Large file operations (500MB) on the main thread caused ANR (Application Not Responding)

## Solution Architecture

### High-Level Design
1. **Model Storage Strategy**: Support both external and internal storage locations
2. **Background Processing**: Use Kotlin Coroutines with `Dispatchers.IO` for file operations
3. **Dedicated UI**: Separate screen for the move operation with progress feedback
4. **State Management**: MVVM pattern with proper state/effect handling

### Components

```
┌─────────────────────────────────────────────────────────────┐
│                      SettingsScreen                          │
│  - Shows model storage status                               │
│  - Navigation to MoveModelScreen                            │
└────────────────┬────────────────────────────────────────────┘
                 │ navigateToMoveModel()
                 ▼
┌─────────────────────────────────────────────────────────────┐
│                     MoveModelScreen                          │
│  - Initial state with description                           │
│  - Moving state with progress indicator                     │
│  - Success/Error states                                     │
└────────────────┬────────────────────────────────────────────┘
                 │ Observes
                 ▼
┌─────────────────────────────────────────────────────────────┐
│                   MoveModelViewModel                         │
│  - Manages UI state (isMoving, errorMessage)               │
│  - Emits effects (Success, Error)                          │
│  - Runs file operations on Dispatchers.IO                  │
└────────────────┬────────────────────────────────────────────┘
                 │ Calls
                 ▼
┌─────────────────────────────────────────────────────────────┐
│              Transcriber.moveModelToProtectedStorage()       │
│  - Wrapped in withContext(Dispatchers.IO)                  │
│  - Streams file copy (inputStream → outputStream)          │
│  - Deletes external file after successful copy             │
│  - Saves internal path to preferences                      │
└─────────────────────────────────────────────────────────────┘
```

## Technical Implementation

### 1. File Storage Strategy

#### Storage Locations
```kotlin
// External Storage (cleared by OS)
val externalPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

// Internal Storage (protected)
val internalPath = context.filesDir
```

#### Model Existence Check
```kotlin
fun doesModelExists(modelFileName: String): Boolean {
    // Check internal storage first (protected)
    val internalFile = File(context.filesDir, modelFileName)
    if (internalFile.exists()) {
        return true
    }

    // Fall back to external storage
    val externalFile = File(modelsPath, modelFileName)
    return externalFile.exists()
}
```

### 2. Non-Blocking File Copy

#### Before (Blocking)
```kotlin
// This blocked the main thread for ~30-60 seconds
externalFile.copyTo(internalFile, overwrite = true)
```

#### After (Non-Blocking)
```kotlin
suspend fun moveModelToProtectedStorage(modelFileName: String): Result<String> {
    return withContext(Dispatchers.IO) {
        try {
            val externalFile = File(modelsPath, modelFileName)
            val internalFile = File(context.filesDir, modelFileName)

            // Stream copy - doesn't load entire 500MB into memory
            externalFile.inputStream().use { input ->
                internalFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            if (internalFile.exists()) {
                preferencesRepository.setModelFilePath(internalFile.absolutePath)
                externalFile.delete()
                Result.success(internalFile.absolutePath)
            } else {
                Result.failure(Exception("Failed to copy model"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### 3. ViewModel State Management

```kotlin
data class MoveModelUiState(
    val modelFileName: String = "",
    val isMoving: Boolean = false,
    val errorMessage: String? = null
)

sealed class MoveModelEffect {
    data class Success(val message: String) : MoveModelEffect()
    data class Error(val message: String) : MoveModelEffect()
}

class MoveModelViewModel(
    private val transcriber: Transcriber,
    private val modelSelection: ModelSelection
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoveModelUiState())
    val uiState: StateFlow<MoveModelUiState> = _uiState

    private val _effects = MutableSharedFlow<MoveModelEffect>()
    val effects: SharedFlow<MoveModelEffect> = _effects

    fun moveModelToProtectedStorage() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isMoving = true, errorMessage = null) }

            val result = transcriber.moveModelToProtectedStorage(
                uiState.value.modelFileName
            )

            _uiState.update { it.copy(isMoving = false) }

            if (result.isSuccess) {
                _effects.emit(MoveModelEffect.Success("Model moved successfully"))
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Unknown error"
                _effects.emit(MoveModelEffect.Error(errorMsg))
                _uiState.update { it.copy(errorMessage = errorMsg) }
            }
        }
    }
}
```

### 4. UI Implementation

#### SettingsScreen - Navigation Entry Point
```kotlin
@Composable
fun ModelStorageSection(
    navigateToMoveModel: () -> Unit,
    // ...
) {
    val isInExternalStorage = transcriber.doesModelExistInExternalStorage(modelFileName)
    val isInInternalStorage = transcriber.doesModelExistInInternalStorage(modelFileName)

    Card(
        modifier = Modifier.clickable(enabled = isInExternalStorage) {
            navigateToMoveModel()
        }
    ) {
        if (isInInternalStorage) {
            // Show "✓ Model in protected storage"
        } else {
            // Show "Move to protected storage" with arrow
        }
    }
}
```

#### MoveModelScreen - Dedicated Move UI
```kotlin
@Composable
fun MoveModelScreen(
    navigateBack: () -> Unit,
    viewModel: MoveModelViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is MoveModelEffect.Success -> showSuccess = true
                is MoveModelEffect.Error -> /* show error */
            }
        }
    }

    when {
        showSuccess -> SuccessContent(navigateBack)
        uiState.isMoving -> MovingContent()
        else -> InitialContent(onMoveClick = {
            viewModel.moveModelToProtectedStorage()
        })
    }
}
```

## Files Created/Modified

### New Files
1. **`MoveModelViewModel.kt`**
   - Location: `shared/src/commonMain/kotlin/com/module/notelycompose/modelDownloader/`
   - Purpose: ViewModel for move model screen with state management

2. **`MoveModelScreen.kt`**
   - Location: `shared/src/commonMain/kotlin/com/module/notelycompose/notes/ui/settings/`
   - Purpose: Dedicated UI for moving model to protected storage

3. **`MOVE_MODEL_IMPLEMENTATION.md`** (this file)
   - Location: Project root
   - Purpose: Implementation documentation

### Modified Files

1. **`Transcriper.android.kt`**
   - Updated `moveModelToProtectedStorage()` to use `withContext(Dispatchers.IO)`
   - Changed from blocking `copyTo()` to streaming copy
   - Added `doesModelExistInExternalStorage()` and `doesModelExistInInternalStorage()`

2. **`Transcriber.kt`** (expect class)
   - Added `moveModelToProtectedStorage()` signature
   - Added `doesModelExistInExternalStorage()` signature
   - Added `doesModelExistInInternalStorage()` signature

3. **`Transcriper.ios.kt`**
   - Added stub implementations for new methods

4. **`PreferencesRepository.kt`**
   - Added `KEY_MODEL_FILE_PATH` preference key
   - Added `getModelFilePath()` and `setModelFilePath()` methods

5. **`SettingsScreen.kt`**
   - Simplified `ModelStorageSection` to navigation card
   - Added `navigateToMoveModel` parameter
   - Removed inline move functionality

6. **`Routes.kt`**
   - Added `Routes.MoveModel` route

7. **`App.kt`**
   - Added navigation route for `MoveModelScreen`
   - Wired up navigation from Settings

8. **`Modules.kt`**
   - Added `MoveModelViewModel` to DI

## Usage Flow

### User Experience

1. **Discovery**
   - User opens Settings
   - If model exists in external storage, sees "Move to protected storage" card
   - Taps card to navigate to dedicated screen

2. **Move Operation**
   - Dedicated screen explains benefits and requirements
   - User taps "Move Now" button
   - Large progress indicator shows during ~30-60 second copy
   - Success screen confirms completion

3. **Confirmation**
   - Returns to Settings
   - Card now shows "✓ Model in protected storage"
   - Model safe from OS cleanup

### Model Switching

The implementation supports multiple models:
- Each model file is checked independently
- User can move each model separately
- Switching models shows correct storage status
- Example:
  - `ggml-base-en.bin` in external storage → show move option
  - `ggml-small.bin` in internal storage → show checkmark
  - Switching between them updates UI correctly

## Performance Considerations

### File Copy Performance
- **File Size**: ~500MB (optimized model) or ~142MB (standard model)
- **Copy Time**: 30-60 seconds depending on device
- **Memory Usage**: Streaming copy uses minimal memory (~8KB buffer)
- **Thread Impact**: Runs on IO dispatcher, no main thread blocking

### State Updates
- UI updates happen on main thread via `StateFlow`
- Heavy operations (file I/O) run on `Dispatchers.IO`
- Progress feedback keeps user informed

## Testing Recommendations

### Manual Testing
1. **Basic Flow**
   - Download model in external storage
   - Navigate to move screen
   - Move to protected storage
   - Verify success message
   - Return to Settings and verify checkmark

2. **Model Switching**
   - Download standard model → move to protected storage
   - Switch to optimized model → verify move option appears
   - Move optimized model → verify both in protected storage

3. **Error Handling**
   - Insufficient storage space
   - Permission issues
   - Interrupted operation

4. **OS-Specific Testing**
   - Test on GrapheneOS
   - Test on MIUI
   - Test on stock Android

### Automated Testing
```kotlin
class MoveModelViewModelTest {
    @Test
    fun `moveModelToProtectedStorage success updates state correctly`() {
        // Given
        val viewModel = MoveModelViewModel(mockTranscriber, mockModelSelection)

        // When
        viewModel.moveModelToProtectedStorage()

        // Then
        assertTrue(viewModel.uiState.value.isMoving)
        // Wait for completion
        assertEquals(false, viewModel.uiState.value.isMoving)
    }
}
```

## Benefits

### User Benefits
✅ **No repeated downloads** - Model stays on device
✅ **Clear communication** - Explains why and when to use feature
✅ **Non-intrusive** - Optional feature, only shown when needed
✅ **Progress feedback** - Clear indication during long operation

### Technical Benefits
✅ **No ANR** - File operations don't block main thread
✅ **Memory efficient** - Streaming copy doesn't load entire file
✅ **Proper error handling** - Failed operations don't crash app
✅ **Clean architecture** - MVVM with proper separation of concerns
✅ **Multi-model support** - Each model tracked independently

## Future Enhancements

### Potential Improvements
1. **Progress Percentage** - Show copy progress (0-100%)
2. **Automatic Migration** - Move on first download if user opts in
3. **Storage Analysis** - Show available space before move
4. **Batch Move** - Move all models at once
5. **Undo Option** - Move back to external storage if needed

## Troubleshooting

### Common Issues

**Issue: Move operation fails with "Insufficient storage"**
- Solution: Verify device has ~500MB+ free internal storage

**Issue: Model not found after move**
- Solution: Check `doesModelExistInInternalStorage()` returns true
- Verify preferences contain correct path

**Issue: UI freezes during move**
- Solution: Verify operation runs on `Dispatchers.IO`
- Check no blocking operations on main thread

## References

### Related Files
- External storage: `Downloader.android.kt`
- Model selection: `ModelSelection.kt`
- Download flow: `ModelDownloaderViewModel.kt`

### Android Documentation
- [Data and file storage](https://developer.android.com/training/data-storage)
- [Coroutines on Android](https://developer.android.com/kotlin/coroutines)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
