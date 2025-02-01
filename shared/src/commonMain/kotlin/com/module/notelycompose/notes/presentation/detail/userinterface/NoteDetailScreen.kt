package com.module.notelycompose.notes.presentation.detail.userinterface

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.module.notelycompose.getPlatform
import com.module.notelycompose.notes.presentation.theme.LocalCustomColors
import com.module.notelycompose.resources.vectors.IcChevronLeft
import com.module.notelycompose.resources.vectors.IcRecorder
import com.module.notelycompose.resources.vectors.Images

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteDetailScreen(
    title: String? = null,
    content: String? = null,
    newNoteDateString: String,
    onSaveClick: (String, String, Boolean) -> Unit,
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
    var contentState by remember { mutableStateOf(TextFieldValue(content ?: "")) }
    val isUpdateScreen = remember { mutableStateOf(title != null) }
    var showFormatBar by remember { mutableStateOf(false) }
    val formatKey = editorState.formats.hashCode()

    Scaffold(
        topBar = {
            if (getPlatform().isAndroid) {
                TopAppBar(
                    title = { Text("My Note") },
                    navigationIcon = {
                        IconButton(onClick = { onNavigateBack() }) {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    backgroundColor = LocalCustomColors.current.bodyBackgroundColor,
                    contentColor = LocalCustomColors.current.bodyContentColor
                )
            } else {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                onNavigateBack()
                            }
                        ) {
                            Icon(
                                imageVector = Images.Icons.IcChevronLeft,
                                contentDescription = "Back",
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Back",
                                style = MaterialTheme.typography.body1,
                            )
                        }
                    },
                    contentColor = LocalCustomColors.current.iOSBackButtonColor,
                    backgroundColor = LocalCustomColors.current.transparentColor,
                    modifier = Modifier.padding(start = 0.dp),
                    elevation = 0.dp,
                )
            }
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
                onClick = { /* Add Note */ }
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
                selectionSize = editorState.selectionSize
            )
        },
        content = { paddingValues ->
            Box (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .background(LocalCustomColors.current.bodyBackgroundColor)
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

                    key(formatKey) {
                        BasicTextField(
                            value = editorState.content,
                            onValueChange = { onUpdateContent(it) },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            textStyle = TextStyle(
                                color = LocalCustomColors.current.bodyContentColor,
                                textAlign = editorState.textAlign
                            ),
                            cursorBrush = SolidColor(LocalCustomColors.current.bodyContentColor),
                            readOnly = showFormatBar,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences
                            ),
                            visualTransformation = transformation
                        )
                    }
                }
            }
        }
    )
}

