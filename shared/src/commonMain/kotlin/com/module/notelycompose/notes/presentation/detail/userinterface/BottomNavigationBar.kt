package com.module.notelycompose.notes.presentation.detail.userinterface

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.module.notelycompose.notes.presentation.theme.LocalCustomColors
import com.module.notelycompose.notes.presentation.extensions.showKeyboard
import com.module.notelycompose.resources.vectors.IcDetailList
import com.module.notelycompose.resources.vectors.IcHeart
import com.module.notelycompose.resources.vectors.IcKeyboardHide
import com.module.notelycompose.resources.vectors.IcLetterAa
import com.module.notelycompose.resources.vectors.Images

private const val ZERO_DENSITY = 0
@Composable
fun BottomNavigationBar(
    onToggleBold: () -> Unit,
    onToggleItalic: () -> Unit,
    onToggleUnderline: () -> Unit,
    onSetAlignment: (alignment: TextAlign) -> Unit,
    onToggleBulletList: () -> Unit,
    showFormatBar: Boolean,
    onShowTextFormatBar: (show: Boolean) -> Unit,
    onDeleteNote: () -> Unit,
    onSelectTextSizeFormat: (textSize: Float) -> Unit,
    selectionSize: TextFormatUiOption,
    textFieldFocusRequester: FocusRequester
) {
    var selectedFormat by remember { mutableStateOf(FormatOptionTextFormat.Body) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > ZERO_DENSITY

    when(selectionSize) {
        TextUiFormats.Title -> selectedFormat = FormatOptionTextFormat.Title
        TextUiFormats.Heading -> selectedFormat = FormatOptionTextFormat.Heading
        TextUiFormats.SubHeading -> selectedFormat = FormatOptionTextFormat.Subheading
        TextUiFormats.Body -> selectedFormat = FormatOptionTextFormat.Body
        else -> Unit
    }

    DeleteConfirmationDialog(
        showDialog = showDeleteDialog,
        onDismiss = { showDeleteDialog = false },
        onConfirm = onDeleteNote
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AnimatedVisibility(
            visible = showFormatBar,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            FormatBar(
                selectedFormat = selectedFormat,
                onFormatSelected = {
                    selectedFormat = it
                    textSizeSelectedFormats(it) { textSize ->
                        onSelectTextSizeFormat(textSize)
                    }
                },
                onClose = { onShowTextFormatBar(false) },
                onToggleBold = { onToggleBold() },
                onToggleItalic = { onToggleItalic() },
                onToggleUnderline = { onToggleUnderline() },
                onSetAlignment = { alignment ->
                    onSetAlignment(alignment)
                }
            )
        }
    }

    if (!showFormatBar) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalCustomColors.current.bodyBackgroundColor)
                .padding(8.dp)
                .padding(start = 8.dp, end = 48.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                onShowTextFormatBar(true)
            }) {
                Icon(
                    imageVector = Images.Icons.IcLetterAa,
                    contentDescription = "Letter",
                    tint = LocalCustomColors.current.bodyContentColor
                )
            }
            IconButton(onClick = { onToggleBulletList() }) {
                Icon(
                    imageVector = Images.Icons.IcDetailList,
                    contentDescription = "Type",
                    tint = LocalCustomColors.current.bodyContentColor
                )
            }
            IconButton(onClick = { /* Type */ }) {
                Icon(
                    imageVector = Images.Icons.IcHeart,
                    contentDescription = "Type",
                    tint = LocalCustomColors.current.bodyContentColor
                )
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete Note",
                    tint = LocalCustomColors.current.bodyContentColor
                )
            }
            IconButton(onClick = {
                textFieldFocusRequester.showKeyboard(imeVisible, keyboardController)
            }) {
                Icon(
                    imageVector = Images.Icons.IcKeyboardHide,
                    contentDescription = "Hide Keyboard",
                    tint = LocalCustomColors.current.bodyContentColor
                )
            }
        }
    }
}

