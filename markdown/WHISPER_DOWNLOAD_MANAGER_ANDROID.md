# Whisper Model Download Manager - Android Implementation

## Overview

This document describes the Android implementation of a robust model download manager with comprehensive error handling and user-friendly dialogs. The system uses Android's DownloadManager API with Kotlin Coroutines and Jetpack Compose.

---

## Architecture Components

1. **Downloader (Android)** - Download management using Android DownloadManager API
2. **DownloadErrorMapper** - Centralized error code mapping
3. **ModelDownloaderViewModel** - State management and orchestration
4. **Dialog Components** - Compose UI for download progress and confirmation
5. **InsufficientStorageException** - Custom exception for storage errors

---

## 1. Error Management System

### 1.1 Error Code Constants

**File: `DownloadErrorMapper.kt`**

```kotlin
class DownloadErrorMapper {

    companion object {
        // Storage errors
        const val ERROR_INSUFFICIENT_SPACE = "ERROR_INSUFFICIENT_SPACE"
        const val ERROR_DEVICE_NOT_FOUND = "ERROR_DEVICE_NOT_FOUND"
        const val ERROR_CANNOT_CREATE_FILE = "ERROR_CANNOT_CREATE_FILE"
        const val ERROR_CANNOT_WRITE_FILE = "ERROR_CANNOT_WRITE_FILE"
        const val ERROR_FILE_ERROR = "ERROR_FILE_ERROR"
        const val ERROR_FILE_NOT_FOUND = "ERROR_FILE_NOT_FOUND"
        const val ERROR_FILE_ALREADY_EXISTS = "ERROR_FILE_ALREADY_EXISTS"

        // Network errors
        const val ERROR_NO_INTERNET = "ERROR_NO_INTERNET"
        const val ERROR_TIMEOUT = "ERROR_TIMEOUT"
        const val ERROR_CANNOT_CONNECT = "ERROR_CANNOT_CONNECT"
        const val ERROR_CONNECTION_LOST = "ERROR_CONNECTION_LOST"
        const val ERROR_CANNOT_FIND_HOST = "ERROR_CANNOT_FIND_HOST"
        const val ERROR_DATA_NOT_ALLOWED = "ERROR_DATA_NOT_ALLOWED"

        // HTTP errors
        const val ERROR_UNHANDLED_HTTP_CODE = "ERROR_UNHANDLED_HTTP_CODE"
        const val ERROR_TOO_MANY_REDIRECTS = "ERROR_TOO_MANY_REDIRECTS"
        const val ERROR_HTTP_DATA_ERROR = "ERROR_HTTP_DATA_ERROR"
        const val ERROR_BAD_RESPONSE = "ERROR_BAD_RESPONSE"

        // Operation errors
        const val ERROR_CANCELLED = "ERROR_CANCELLED"
        const val ERROR_CANNOT_RESUME = "ERROR_CANNOT_RESUME"
        const val ERROR_PERMISSION_DENIED = "ERROR_PERMISSION_DENIED"
        const val ERROR_DECODING_FAILED = "ERROR_DECODING_FAILED"
        const val ERROR_UNKNOWN = "ERROR_UNKNOWN"
        const val DOWNLOAD_ERROR = "DOWNLOAD_ERROR"
    }
}
```

### 1.2 Error Message Mapper

Converts error codes to user-friendly messages:

```kotlin
class DownloadErrorMapper {

    fun mapToFriendlyMessage(errorCode: String): String {
        return when (errorCode) {
            ERROR_INSUFFICIENT_SPACE ->
                "Insufficient storage space. Please free up space and try again."
            ERROR_CANNOT_RESUME ->
                "Cannot resume download. Please try again."
            ERROR_DEVICE_NOT_FOUND ->
                "Storage device not found."
            ERROR_FILE_ALREADY_EXISTS ->
                "File already exists."
            ERROR_FILE_ERROR ->
                "File error occurred."
            ERROR_HTTP_DATA_ERROR ->
                "Network data error. Please check your connection."
            ERROR_TOO_MANY_REDIRECTS ->
                "Too many redirects. Please try again."
            ERROR_UNHANDLED_HTTP_CODE ->
                "Server error. Please try again later."
            ERROR_CANCELLED ->
                "Download cancelled"
            ERROR_TIMEOUT ->
                "Connection timed out. Please try again."
            ERROR_CANNOT_CONNECT ->
                "Cannot connect to host. Please check your connection."
            ERROR_CONNECTION_LOST ->
                "Network connection lost. Please try again."
            ERROR_NO_INTERNET ->
                "No internet connection. Please check your network."
            ERROR_BAD_RESPONSE ->
                "Bad server response. Please try again later."
            ERROR_PERMISSION_DENIED ->
                "Permission denied."
            ERROR_FILE_NOT_FOUND ->
                "File not found."
            ERROR_CANNOT_CREATE_FILE ->
                "Cannot create file. Storage may be full."
            ERROR_CANNOT_WRITE_FILE ->
                "Cannot write file. Storage may be full."
            ERROR_DECODING_FAILED ->
                "Download decoding failed."
            ERROR_CANNOT_FIND_HOST ->
                "Cannot find host. Please check the URL."
            ERROR_DATA_NOT_ALLOWED ->
                "Data not allowed. Please check your network settings."
            ERROR_UNKNOWN ->
                "An unknown error occurred."
            DOWNLOAD_ERROR ->
                "Download failed. Please try again."
            else ->
                "Download failed. Please try again."
        }
    }
}
```

### 1.3 Custom Storage Exception

**File: `InsufficientStorageException.kt`**

```kotlin
class InsufficientStorageException(val errorCode: String) : Exception(errorCode)
```

This exception is thrown during pre-download storage validation and can be caught separately for special handling.

### 1.4 Android DownloadManager Error Mapping

Maps Android DownloadManager error codes to unified error constants:

```kotlin
private fun getErrorTextFromReason(reason: Int): String {
    val errorCode = when (reason) {
        DownloadManager.ERROR_CANNOT_RESUME ->
            DownloadErrorMapper.ERROR_CANNOT_RESUME
        DownloadManager.ERROR_DEVICE_NOT_FOUND ->
            DownloadErrorMapper.ERROR_DEVICE_NOT_FOUND
        DownloadManager.ERROR_FILE_ALREADY_EXISTS ->
            DownloadErrorMapper.ERROR_FILE_ALREADY_EXISTS
        DownloadManager.ERROR_FILE_ERROR ->
            DownloadErrorMapper.ERROR_FILE_ERROR
        DownloadManager.ERROR_HTTP_DATA_ERROR ->
            DownloadErrorMapper.ERROR_HTTP_DATA_ERROR
        DownloadManager.ERROR_INSUFFICIENT_SPACE ->
            DownloadErrorMapper.ERROR_INSUFFICIENT_SPACE
        DownloadManager.ERROR_TOO_MANY_REDIRECTS ->
            DownloadErrorMapper.ERROR_TOO_MANY_REDIRECTS
        DownloadManager.ERROR_UNHANDLED_HTTP_CODE ->
            DownloadErrorMapper.ERROR_UNHANDLED_HTTP_CODE
        DownloadManager.ERROR_UNKNOWN ->
            DownloadErrorMapper.ERROR_UNKNOWN
        else ->
            DownloadErrorMapper.DOWNLOAD_ERROR
    }
    return errorMapper.mapToFriendlyMessage(errorCode)
}
```

---

## 2. Android Downloader Implementation

### 2.1 Class Structure

**File: `Downloader.android.kt`**

```kotlin
actual class Downloader(
    private val mainContext: Context,
    private val preferencesRepository: PreferencesRepository,
    private val errorMapper: DownloadErrorMapper
) {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    // Public API methods
    actual suspend fun startDownload(url: String, fileName: String, fileSizeMB: Long)
    actual suspend fun hasRunningDownload(): Boolean
    actual suspend fun trackDownloadProgress(...)
    actual suspend fun cancelDownload()

    // Private helper methods
    private fun hasEnoughStorage(requiredSizeMB: Long): Boolean
    private fun registerDownloadReceiver(...)
    private fun getErrorTextFromReason(reason: Int): String
}
```

### 2.2 Storage Validation

Checks available storage with a 50MB safety buffer:

```kotlin
private fun hasEnoughStorage(requiredSizeMB: Long): Boolean {
    if (requiredSizeMB <= 0) {
        return true
    }

    try {
        val downloadDir = mainContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        if (downloadDir != null) {
            val stat = StatFs(downloadDir.path)
            val availableBytes = stat.availableBlocksLong * stat.blockSizeLong
            val availableMB = availableBytes / (1024 * 1024)
            val requiredWithBuffer = requiredSizeMB + 50  // 50MB safety buffer

            Napier.d("Available storage: ${availableMB}MB, Required: ${requiredWithBuffer}MB")
            return availableMB >= requiredWithBuffer
        }
    } catch (e: Exception) {
        Napier.e("Error checking storage: ${e.message}")
        return true  // Allow download to proceed if check fails
    }
    return false
}
```

**Key Points**:
- Uses `StatFs` to check available storage on download directory
- Adds 50MB buffer for system overhead and safety
- Returns `true` if check fails (fail-safe approach)
- Logs storage information for debugging

### 2.3 Start Download

Initiates download with pre-flight storage check:

```kotlin
actual suspend fun startDownload(url: String, fileName: String, fileSizeMB: Long) {
    try {
        // Pre-download storage validation
        if (!hasEnoughStorage(fileSizeMB)) {
            Napier.e("Insufficient storage space. Required: ${fileSizeMB}MB")
            throw InsufficientStorageException(DownloadErrorMapper.ERROR_INSUFFICIENT_SPACE)
        }

        // Configure download request
        val request = DownloadManager.Request(url.toUri())
            .setTitle("Downloading $fileName")
            .setDestinationInExternalFilesDir(
                mainContext,
                Environment.DIRECTORY_DOWNLOADS,
                fileName
            )
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            .setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI or
                DownloadManager.Request.NETWORK_MOBILE
            )
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        // Enqueue download
        val downloadManager =
            mainContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)

        // Persist download ID for tracking
        preferencesRepository.setModelDownloadId(downloadId)

    } catch (e: InsufficientStorageException) {
        throw e  // Re-throw to be handled by ViewModel
    } catch (e: NullPointerException) {
        debugPrintln {"Invalid download URL $url: ${e.message}"}
        throw e
    } catch (e: Exception) {
        debugPrintln {"Failed to start download: ${e.message}"}
        throw e
    }
}
```

**Configuration Details**:
- **Destination**: External files directory (app-specific, no permissions needed)
- **Notifications**: Hidden (we manage UI ourselves)
- **Network**: Both WiFi and Mobile allowed
- **Metered/Roaming**: Allowed (user has already confirmed download)

### 2.4 Check Running Download

Validates if a download is currently active:

```kotlin
actual suspend fun hasRunningDownload(): Boolean {
    val downloadId = preferencesRepository.getModelDownloadId().first()
    if (downloadId == -1L) {
        return false
    }

    val downloadManager =
        mainContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val query = DownloadManager.Query().setFilterById(downloadId)
    val cursor = downloadManager.query(query)

    cursor.use {
        if (it.moveToFirst()) {
            val status = it.getInt(
                it.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS)
            )
            val bytesTotal = it.getLong(
                it.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
            )

            // Check if download is stuck (PENDING with no total size)
            if (status == DownloadManager.STATUS_PENDING && bytesTotal <= 0) {
                try {
                    downloadManager.remove(downloadId)
                    preferencesRepository.setModelDownloadId(-1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return false
            }

            // Check if download failed
            if (status == DownloadManager.STATUS_FAILED) {
                try {
                    downloadManager.remove(downloadId)
                    preferencesRepository.setModelDownloadId(-1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return false
            }

            return status == DownloadManager.STATUS_RUNNING ||
                   status == DownloadManager.STATUS_PENDING ||
                   status == DownloadManager.STATUS_PAUSED
        } else {
            preferencesRepository.setModelDownloadId(-1)
        }
    }
    return false
}
```

**Cleanup Logic**:
- Removes stuck downloads (PENDING with no size)
- Removes failed downloads
- Cleans up download ID when download no longer exists

### 2.5 Track Download Progress

Core progress tracking with stuck download detection:

```kotlin
actual suspend fun trackDownloadProgress(
    fileName: String,
    onProgressUpdated: (progress: Int, downloadedMB: String, totalMB: String) -> Unit,
    onSuccess: () -> Unit,
    onFailed: (String) -> Unit,
) {
    val downloadId = preferencesRepository.getModelDownloadId().first()
    if (downloadId == -1L) {
        return
    }

    // Register BroadcastReceiver for completion
    registerDownloadReceiver(downloadId, onSuccess, onFailed)

    val downloadManager =
        mainContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    var stuckPendingCount = 0
    val maxStuckIterations = 15  // 15 seconds timeout

    while (true) {
        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor: Cursor = downloadManager.query(query)

        if (cursor.moveToFirst()) {
            val bytesDownloaded = cursor.getLong(
                cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
            )
            val bytesTotal = cursor.getLong(
                cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
            )
            val status = cursor.getInt(
                cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS)
            )

            // Stuck download detection
            if (status == DownloadManager.STATUS_PENDING && bytesTotal <= 0) {
                stuckPendingCount++

                if (stuckPendingCount >= maxStuckIterations) {
                    cursor.close()
                    downloadManager.remove(downloadId)
                    preferencesRepository.setModelDownloadId(-1)
                    onFailed("Download stuck in PENDING state. Please check your network connection and try again.")
                    break
                }
            } else {
                stuckPendingCount = 0  // Reset counter if download is progressing
            }

            // Update progress
            if (bytesTotal > 0) {
                val progress = (bytesDownloaded * 100L / bytesTotal).toInt()

                // Convert to MB with 2 decimal places
                val downloadedMB = String.format("%.2f MB", bytesDownloaded / 1024.0 / 1024.0)
                val totalMB = String.format("%.2f MB", bytesTotal / 1024.0 / 1024.0)

                onProgressUpdated(progress, downloadedMB, totalMB)
            }

            cursor.close()

            // Exit on completion or failure
            if (status == DownloadManager.STATUS_SUCCESSFUL ||
                status == DownloadManager.STATUS_FAILED) {
                break
            }
        } else {
            cursor.close()
            break
        }

        delay(1000)  // Poll every second
    }
}
```

**Key Features**:
- **Stuck Detection**: 15-second timeout for PENDING state with no progress
- **Polling**: 1-second interval for responsive UI updates
- **Progress Format**: MB with 2 decimal places
- **Auto-cleanup**: Removes stuck downloads automatically

### 2.6 BroadcastReceiver for Completion

Handles download completion events:

```kotlin
private fun registerDownloadReceiver(
    downloadId: Long,
    onSuccess: () -> Unit,
    onFailed: (String) -> Unit
) {
    val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE).apply {
        addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
    }

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (id == downloadId) {
                when (intent.action) {
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE -> {
                        coroutineScope.launch {
                            preferencesRepository.setModelDownloadId(-1)
                        }

                        val downloadManager =
                            context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        val query = DownloadManager.Query().setFilterById(downloadId)
                        val cursor = downloadManager.query(query)

                        if (cursor.moveToFirst()) {
                            val status = cursor.getInt(
                                cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS)
                            )

                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                // Get downloaded file URI
                                val uriString = cursor.getString(
                                    cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI)
                                )
                                val uri = uriString.toUri()
                                debugPrintln {"Download complete: $uri"}
                                onSuccess()
                            } else {
                                // Handle failed download
                                val reason = cursor.getInt(
                                    cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON)
                                )
                                debugPrintln{"Download failed: $reason"}
                                val errorText = getErrorTextFromReason(reason)
                                onFailed(errorText)
                            }
                        }
                        cursor.close()
                    }

                    DownloadManager.ACTION_NOTIFICATION_CLICKED -> {
                        debugPrintln{"Opening downloads..."}
                    }
                }

                // Cleanup
                mainContext.unregisterReceiver(this)
                coroutineScope.cancel()
            }
        }
    }

    ContextCompat.registerReceiver(
        mainContext,
        receiver,
        filter,
        ContextCompat.RECEIVER_EXPORTED
    )
}
```

**Receiver Lifecycle**:
1. Registered when tracking starts
2. Waits for download completion broadcast
3. Verifies download ID matches
4. Checks success/failure status
5. Calls appropriate callback
6. Unregisters itself and cancels coroutine scope

### 2.7 Cancel Download

Stops and removes the download:

```kotlin
actual suspend fun cancelDownload() {
    val downloadId = preferencesRepository.getModelDownloadId().first()

    if (downloadId != -1L) {
        try {
            val downloadManager =
                mainContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.remove(downloadId)
            preferencesRepository.setModelDownloadId(-1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
```

**Cancellation Process**:
1. Retrieve stored download ID
2. Remove download from DownloadManager
3. Clear stored download ID
4. BroadcastReceiver will not trigger (different download ID)

---

## 3. UI State Management

### 3.1 UI State Model

**File: `DownloaderUiState.kt`**

```kotlin
data class DownloaderUiState(
    val selectedModel: TranscriptionModel,
    val downloading: Boolean = false,
    val progress: Float = 0f,
    val downloaded: String = "0 MB",
    val total: String = "0 MB"
)
```

**State Properties**:
- `selectedModel`: Current model being downloaded
- `downloading`: Download in progress flag
- `progress`: Progress percentage (0-100)
- `downloaded`: Downloaded bytes in MB format
- `total`: Total file size in MB format

### 3.2 Effect Sealed Class

```kotlin
sealed class DownloaderEffect {
    class DownloadEffect : DownloaderEffect()
    class ModelsAreReady : DownloaderEffect()
    class AskForUserAcceptance : DownloaderEffect()
    class ErrorEffect(val message: String? = null) : DownloaderEffect()
    class CheckingEffect : DownloaderEffect()
}
```

**Effect Types**:
- `CheckingEffect`: Verifying model availability
- `AskForUserAcceptance`: Show download confirmation dialog
- `DownloadEffect`: Show download progress dialog
- `ModelsAreReady`: Download complete, model initialized
- `ErrorEffect`: Show error dialog with message

---

## 4. ViewModel Implementation

### 4.1 ModelDownloaderViewModel

**File: `ModelDownloaderViewModel.kt`**

```kotlin
class ModelDownloaderViewModel(
    private val downloader: Downloader,
    private val transcriber: Transcriber,
    private val modelSelection: ModelSelection,
    private val errorMapper: DownloadErrorMapper
): ViewModel() {

    private var _uiState: MutableStateFlow<DownloaderUiState> =
        MutableStateFlow(DownloaderUiState(modelSelection.getDefaultTranscriptionModel()))
    val uiState: StateFlow<DownloaderUiState> = _uiState

    private val _effects = MutableSharedFlow<DownloaderEffect>()
    val effects: SharedFlow<DownloaderEffect> = _effects

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedModel = modelSelection.getSelectedModel()
            _uiState.value = DownloaderUiState(selectedModel)
        }
    }
}
```

### 4.2 Check Model Availability

```kotlin
fun checkTranscriptionAvailability() {
    viewModelScope.launch(Dispatchers.IO) {
        _effects.emit(DownloaderEffect.CheckingEffect())

        if (downloader.hasRunningDownload()) {
            // Resume existing download
            trackDownload()
        } else {
            // Check if model exists and is valid
            if (!transcriber.doesModelExists(uiState.value.selectedModel.name) ||
                !transcriber.isValidModel(uiState.value.selectedModel.name)) {
                _effects.emit(DownloaderEffect.AskForUserAcceptance())
            } else {
                _effects.emit(DownloaderEffect.ModelsAreReady())
            }
        }
    }
}
```

### 4.3 Start Download

```kotlin
fun startDownload(model: TranscriptionModel? = null) {
    viewModelScope.launch(Dispatchers.IO) {
        try {
            val selectedModel = model ?: uiState.value.selectedModel

            // Update UI state with new model if provided
            if (model != null) {
                _uiState.update { current ->
                    current.copy(selectedModel = model)
                }
            }

            val modelUrl = selectedModel.url
            val modelSize = selectedModel.getSizeInMB()

            // Start download (may throw InsufficientStorageException)
            downloader.startDownload(modelUrl, selectedModel.name, modelSize)

            // Begin tracking
            trackDownload()

        } catch (e: InsufficientStorageException) {
            val errorMessage = errorMapper.mapToFriendlyMessage(e.errorCode)
            _effects.emit(DownloaderEffect.ErrorEffect(errorMessage))
        } catch (e: Exception) {
            val errorMessage = errorMapper.mapToFriendlyMessage(
                DownloadErrorMapper.DOWNLOAD_ERROR
            )
            _effects.emit(DownloaderEffect.ErrorEffect(errorMessage))
        }
    }
}
```

### 4.4 Track Download Progress

```kotlin
private suspend fun trackDownload() {
    _effects.emit(DownloaderEffect.DownloadEffect())

    downloader.trackDownloadProgress(
        uiState.value.selectedModel.name,

        onProgressUpdated = { progress, downloadedMB, totalMB ->
            _uiState.update { current ->
                current.copy(
                    progress = progress.toFloat(),
                    downloaded = downloadedMB,
                    total = totalMB
                )
            }
        },

        onSuccess = {
            viewModelScope.launch {
                transcriber.initialize(uiState.value.selectedModel.name)
                _effects.emit(DownloaderEffect.ModelsAreReady())
            }
        },

        onFailed = { errorMessage ->
            viewModelScope.launch {
                _effects.emit(DownloaderEffect.ErrorEffect(errorMessage))
            }
        }
    )
}
```

### 4.5 Cancel Download

```kotlin
fun cancelDownload() {
    viewModelScope.launch(Dispatchers.IO) {
        downloader.cancelDownload()
    }
}
```

---

## 5. Dialog Components

### 5.1 Download Confirmation Dialog

**File: `DownloadModelDialog.kt`**

Shows before download starts:

```kotlin
@Composable
fun DownloadModelDialog(
    onDownload: () -> Unit,
    onCancel: () -> Unit,
    transcriptionModel: TranscriptionModel,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onCancel,
        title = {
            Text(text = "Download Required")
        },
        text = {
            Column {
                Text("For accurate transcription")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Download may take a few minutes")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Model: ${transcriptionModel.description}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("File size: ${transcriptionModel.getModelDownloadSize()}")
            }
        },
        confirmButton = {
            Button(
                onClick = onDownload,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = LocalCustomColors.current.darkBlueAppBgColor,
                    contentColor = Color.White
                )
            ) {
                Text("Download")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = LocalCustomColors.current.darkBlueAppBgColor
                )
            ) {
                Text("Cancel")
            }
        }
    )
}
```

### 5.2 Download Progress Dialog

**File: `DownloaderDialog.kt`**

Active during download:

```kotlin
@Composable
fun DownloaderDialog(
    transcriptionModel: TranscriptionModel,
    downloaderUiState: DownloaderUiState,
    onDismiss: () -> Unit,
    onCancel: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,  // Prevent accidental dismissal
            dismissOnBackPress = false,      // Prevent back button dismissal
            usePlatformDefaultWidth = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            elevation = 12.dp,
            color = LocalCustomColors.current.bodyBackgroundColor
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                // Title
                Text(
                    "Downloading ${transcriptionModel.description}",
                    color = LocalCustomColors.current.bodyContentColor
                )

                // Progress bar with rounded cap
                LinearProgressIndicator(
                    (downloaderUiState.progress / 100),
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth(),
                    strokeCap = StrokeCap.Round
                )

                // Downloaded / Total size display
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        downloaderUiState.downloaded,
                        color = LocalCustomColors.current.bodyContentColor
                    )
                    Text(
                        "/",
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = LocalCustomColors.current.bodyContentColor
                    )
                    Text(
                        downloaderUiState.total,
                        color = LocalCustomColors.current.bodyContentColor
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Cancel button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCancel) {
                        Text(
                            "Cancel",
                            color = LocalCustomColors.current.bodyContentColor
                        )
                    }
                }
            }
        }
    }
}
```

**Dialog Features**:
- **Non-dismissible**: Cannot be dismissed by clicking outside or back button
- **Rounded progress bar**: Uses `StrokeCap.Round` for modern appearance
- **Real-time updates**: Shows downloaded/total in "XX.XX MB / YY.YY MB" format
- **Cancel button**: Only way to dismiss the dialog

---

## 6. Complete Implementation Flow

### 6.1 Download Flow Diagram

```
User Action
   ↓
1. ViewModel.checkTranscriptionAvailability()
   ↓
2. Check if download already running
   ↓ No
3. Check if model exists and is valid
   ↓ No (Model missing or invalid)
4. Emit AskForUserAcceptance effect
   ↓
5. UI shows DownloadModelDialog
   ↓
6. User clicks "Download"
   ↓
7. ViewModel.startDownload()
   ↓
8. Downloader.startDownload()
   ↓
9. Pre-flight storage check
   ↓ Sufficient storage
10. Create DownloadManager.Request
   ↓
11. Enqueue download
   ↓
12. Store download ID
   ↓
13. Emit DownloadEffect
   ↓
14. UI shows DownloaderDialog
   ↓
15. ViewModel.trackDownload()
   ↓
16. Downloader.trackDownloadProgress()
   ↓
17. Register BroadcastReceiver
   ↓
18. Start polling loop (every 1 second)
   ↓
19. Query download progress
   ↓
20. Update UI state with progress
   ↓
21. Check for stuck download (15 sec timeout)
   ↓
22. Continue until completion
   ↓
23. BroadcastReceiver receives completion
   ↓
24. Check status (SUCCESS or FAILED)
   ↓ SUCCESS
25. Call onSuccess callback
   ↓
26. Initialize transcriber with model
   ↓
27. Emit ModelsAreReady effect
   ↓
28. UI dismisses dialog and proceeds
```

### 6.2 Error Handling Flow

```
Download Error Occurs
   ↓
DownloadManager broadcasts completion with FAILED status
   ↓
BroadcastReceiver receives event
   ↓
Query download for COLUMN_REASON
   ↓
getErrorTextFromReason(reason)
   ↓
Map DownloadManager error code to unified constant
   ↓
errorMapper.mapToFriendlyMessage(errorCode)
   ↓
Call onFailed(friendlyMessage)
   ↓
ViewModel receives error in callback
   ↓
Emit ErrorEffect(friendlyMessage)
   ↓
UI shows error dialog to user
```

### 6.3 Stuck Download Detection Flow

```
trackDownloadProgress() polling loop
   ↓
Every 1 second:
   Query download status
   ↓
   Check: status == PENDING && bytesTotal <= 0?
   ↓ Yes
   Increment stuckPendingCount
   ↓
   Check: stuckPendingCount >= 15?
   ↓ Yes (15 seconds stuck)
   Remove download from DownloadManager
   ↓
   Clear download ID
   ↓
   Call onFailed("Download stuck in PENDING state. Please check your network connection and try again.")
   ↓
   Break polling loop
   ↓
   UI shows error dialog
```

### 6.4 Cancel Flow

```
User clicks Cancel button
   ↓
onCancel() callback invoked
   ↓
ViewModel.cancelDownload()
   ↓
Downloader.cancelDownload()
   ↓
Retrieve download ID from preferences
   ↓
downloadManager.remove(downloadId)
   ↓
Clear stored download ID
   ↓
Dialog dismissed
   ↓
BroadcastReceiver does not trigger
(download ID no longer matches)
```

---

## 7. Usage Example

### 7.1 Compose Integration

```kotlin
@Composable
fun TranscriptionScreen() {
    val viewModel: ModelDownloaderViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    // Collect effects as events
    val effects = viewModel.effects.collectAsFlowOfEvents()

    // Check model availability on screen load
    LaunchedEffect(Unit) {
        viewModel.checkTranscriptionAvailability()
    }

    // Handle effects
    effects?.let { effect ->
        when (effect) {
            is DownloaderEffect.CheckingEffect -> {
                // Show loading indicator
                LoadingDialog()
            }

            is DownloaderEffect.AskForUserAcceptance -> {
                DownloadModelDialog(
                    transcriptionModel = uiState.selectedModel,
                    onDownload = { viewModel.startDownload() },
                    onCancel = { /* navigate back or dismiss */ }
                )
            }

            is DownloaderEffect.DownloadEffect -> {
                DownloaderDialog(
                    transcriptionModel = uiState.selectedModel,
                    downloaderUiState = uiState,
                    onDismiss = { /* cannot dismiss */ },
                    onCancel = { viewModel.cancelDownload() }
                )
            }

            is DownloaderEffect.ErrorEffect -> {
                AlertDialog(
                    title = { Text("Download Failed") },
                    text = { Text(effect.message ?: "Unknown error occurred") },
                    confirmButton = {
                        Button(onClick = { /* dismiss */ }) {
                            Text("OK")
                        }
                    }
                )
            }

            is DownloaderEffect.ModelsAreReady -> {
                // Navigate to transcription screen
                // or proceed with transcription
            }
        }
    }
}
```

### 7.2 Helper Extension for Effects

```kotlin
@Composable
fun <T> SharedFlow<T>.collectAsFlowOfEvents(): T? {
    var event by remember { mutableStateOf<T?>(null) }

    LaunchedEffect(Unit) {
        collect { event = it }
    }

    return event
}
```

---

## 8. Critical Implementation Details

### 8.1 Storage Buffer (50MB)

```kotlin
val requiredWithBuffer = requiredSizeMB + 50
```

**Rationale**:
- Prevents failures from file system overhead
- Accounts for temporary extraction/processing
- Provides safety margin for system operations

### 8.2 Stuck Download Timeout (15 seconds)

```kotlin
val maxStuckIterations = 15  // 15 seconds
```

**Rationale**:
- Prevents indefinite waiting
- Detects network failures quickly
- Allows user to retry without long delays

### 8.3 Polling Interval (1 second)

```kotlin
delay(1000)  // Poll every second
```

**Rationale**:
- Responsive UI updates
- Low battery/CPU impact
- Balances UX and performance

### 8.4 Dialog Dismissal Prevention

```kotlin
DialogProperties(
    dismissOnClickOutside = false,
    dismissOnBackPress = false
)
```

**Rationale**:
- Prevents accidental cancellation
- Forces intentional user action
- Ensures user awareness of download state

### 8.5 Download Configuration

```kotlin
.setAllowedNetworkTypes(
    DownloadManager.Request.NETWORK_WIFI or
    DownloadManager.Request.NETWORK_MOBILE
)
.setAllowedOverMetered(true)
.setAllowedOverRoaming(true)
```

**Rationale**:
- User has already confirmed download
- Don't block download on metered networks
- User decision trumps system restrictions

---

## 9. Testing Considerations

### 9.1 Error Scenarios

Test these error conditions:

1. **Insufficient Storage**
   - Fill device storage before download
   - Verify pre-flight check throws `InsufficientStorageException`

2. **Network Errors**
   - Disable WiFi/Mobile during download
   - Verify timeout and stuck detection
   - Verify friendly error message displayed

3. **Invalid URL**
   - Provide malformed URL
   - Verify DownloadManager error handling

4. **Download Cancellation**
   - Cancel at various progress points (10%, 50%, 90%)
   - Verify cleanup (download removed, ID cleared)
   - Verify no error dialog shown

5. **Stuck Download**
   - Simulate PENDING state with no progress
   - Verify 15-second timeout triggers
   - Verify error message and cleanup

### 9.2 Edge Cases

1. **App Restart During Download**
   - Start download
   - Kill app
   - Restart app
   - Verify `hasRunningDownload()` resumes tracking

2. **Large File Downloads**
   - Test with files > 500MB
   - Verify progress updates correctly
   - Verify MB formatting at high values

3. **Rapid Start/Cancel**
   - Start download
   - Immediately cancel
   - Verify no race conditions

4. **Multiple Downloads**
   - Attempt to start second download while first is running
   - Verify proper handling (queue, reject, or replace)

---

## 10. Summary

This Android download manager provides:

✅ **Robust Error Handling**
- 23 distinct error codes
- User-friendly error messages
- Platform-specific error mapping

✅ **Proactive Error Prevention**
- Pre-download storage validation
- 50MB safety buffer
- Stuck download detection (15 seconds)

✅ **Excellent User Experience**
- Real-time progress updates (1-second polling)
- Non-dismissible dialogs during download
- Graceful cancellation with cleanup
- Clear visual feedback (rounded progress bar)

✅ **Production-Ready Architecture**
- MVVM pattern with ViewModel
- StateFlow for UI state
- SharedFlow for one-time effects
- Kotlin Coroutines for async operations

✅ **Android Best Practices**
- DownloadManager API for reliable downloads
- BroadcastReceiver for completion events
- External files directory (no permissions needed)
- Proper resource cleanup (cursor, receiver)

The implementation handles edge cases, network failures, and platform-specific nuances while providing a clean, maintainable codebase.
