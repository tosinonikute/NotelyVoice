package com.module.notelycompose.notes.ui.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.notes.ui.extensions.showKeyboard
import com.module.notelycompose.resources.vectors.IcDetailList
import com.module.notelycompose.resources.vectors.IcKeyboardHide
import com.module.notelycompose.resources.vectors.IcLetterAa
import com.module.notelycompose.resources.vectors.IcStar
import com.module.notelycompose.resources.vectors.IcStarFilled
import com.module.notelycompose.resources.vectors.Images
import notelycompose.composeapp.generated.resources.Res
import notelycompose.composeapp.generated.resources.bottom_navigation_letter
import notelycompose.composeapp.generated.resources.bottom_navigation_bullet_list
import notelycompose.composeapp.generated.resources.bottom_navigation_delete
import notelycompose.composeapp.generated.resources.bottom_navigation_hide_keyboard
import notelycompose.composeapp.generated.resources.bottom_navigation_starred
import org.jetbrains.compose.resources.stringResource

private const val ZERO_DENSITY = 0

@Composable
fun BottomNavigationBar(
    selectionSize: TextFormatUiOption,
    isStarred: Boolean,
    showFormatBar: Boolean,
    textFieldFocusRequester: FocusRequester,
    onFormatActions: NoteFormatActions,
    onShowTextFormatBar: (show: Boolean) -> Unit,
    onDeleteNote: () -> Unit,
    onStarNote: () -> Unit
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
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = showFormatBar,
            enter = fadeIn(
                animationSpec = tween(durationMillis = 250)
            )
        ) {
            FormatBar(
                modifier = Modifier.width(600.dp),
                selectedFormat = selectedFormat,
                onFormatSelected = {
                    selectedFormat = it
                    textSizeSelectedFormats(it) { textSize ->
                        onFormatActions.onSelectTextSizeFormat(textSize)
                    }
                },
                onClose = {
                    onShowTextFormatBar(false)
                },
                onToggleBold = onFormatActions.onToggleBold,
                onToggleItalic = onFormatActions.onToggleItalic,
                onToggleUnderline = onFormatActions.onToggleUnderline,
                onSetAlignment = onFormatActions.onSetAlignment
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
                    contentDescription = stringResource(Res.string.bottom_navigation_letter),
                    tint = LocalCustomColors.current.bodyContentColor
                )
            }
            IconButton(onClick = onFormatActions.onToggleBulletList) {
                Icon(
                    imageVector = Images.Icons.IcDetailList,
                    contentDescription = stringResource(Res.string.bottom_navigation_bullet_list),
                    tint = LocalCustomColors.current.bodyContentColor
                )
            }
            IconButton(onClick = onStarNote) {
                Icon(
                    imageVector = if(isStarred) {
                        Images.Icons.IcStarFilled
                    } else {
                        Images.Icons.IcStar
                    },
                    contentDescription = stringResource(Res.string.bottom_navigation_starred),
                    tint = LocalCustomColors.current.starredColor
                )
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(Res.string.bottom_navigation_delete),
                    tint = LocalCustomColors.current.bodyContentColor
                )
            }
            IconButton(onClick = {
                textFieldFocusRequester.showKeyboard(imeVisible, keyboardController)
            }) {
                Icon(
                    imageVector = Images.Icons.IcKeyboardHide,
                    contentDescription = stringResource(Res.string.bottom_navigation_hide_keyboard),
                    tint = LocalCustomColors.current.bodyContentColor
                )
            }
        }
    }
}
