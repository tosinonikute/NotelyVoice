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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.core.StringUtil.sanitiseText
import com.module.notelycompose.getPlatform
import com.module.notelycompose.notes.presentation.theme.LocalCustomColors
import com.module.notelycompose.resources.vectors.IcChevronLeft
import com.module.notelycompose.resources.vectors.IcDetailAlignCenter
import com.module.notelycompose.resources.vectors.IcDetailAlignLeft
import com.module.notelycompose.resources.vectors.IcDetailAlignRight
import com.module.notelycompose.resources.vectors.IcDetailBold
import com.module.notelycompose.resources.vectors.IcDetailItalic
import com.module.notelycompose.resources.vectors.IcDetailList
import com.module.notelycompose.resources.vectors.IcDetailRedo
import com.module.notelycompose.resources.vectors.IcDetailShare
import com.module.notelycompose.resources.vectors.IcDetailType
import com.module.notelycompose.resources.vectors.IcDetailUnderline
import com.module.notelycompose.resources.vectors.IcDetailUndo
import com.module.notelycompose.resources.vectors.IcRecorder
import com.module.notelycompose.resources.vectors.Images

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteDetailScreen(
    title: String? = null,
    content: String? = null,
    newNoteDateString: String,
    onSaveClick: (String, String, Boolean) -> Unit,
    onNavigateBack: () -> Unit
) {
    var contentState by remember { mutableStateOf(TextFieldValue(content ?: "")) }
    val isUpdateScreen = remember { mutableStateOf(title != null) }

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
            BottomNavigationBar()
        },
        content = { paddingValues ->
            Box(
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
                    // TODO: implement voice recording
//                    Text(
//                        text = "Milan",
//                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp),
//                        color = LocalCustomColors.current.bodyContentColor
//                    )
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        /* TODO: Sound wave animation and ui required here */
//                        IconButton(onClick = { /* Play audio */ }) {
//                            Icon(
//                                imageVector = Icons.Filled.PlayArrow,
//                                contentDescription = "Play",
//                                tint = LocalCustomColors.current.bodyContentColor
//                            )
//                        }
//                        Text(
//                            modifier = Modifier.padding(start = 0.dp, end = 16.dp),
//                            text = "46 sec",
//                            color = LocalCustomColors.current.bodyContentColor
//                        )
//                    }
                    BasicTextField(
                        value = contentState,
                        onValueChange = { newValue ->
                            val shouldUpdateContent = if(isUpdateScreen.value) {
                                isUpdateScreen.value
                            } else {
                                !isUpdateScreen.value && contentState.text.isNotBlank()
                            }
                            contentState = newValue
                            val titleTyped = newValue.text
                            onSaveClick(titleTyped, newValue.text, shouldUpdateContent)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        textStyle = LocalTextStyle.current.copy(color = LocalCustomColors.current.bodyContentColor),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        cursorBrush = SolidColor(LocalCustomColors.current.bodyContentColor)
                    )

                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                            EditingToolbar()
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalCustomColors.current.bodyBackgroundColor)
            .padding(8.dp)
            .padding(start = 8.dp, end = 48.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Share */ }) {
            Icon(
                imageVector = Images.Icons.IcDetailShare,
                contentDescription = "Share",
                tint = LocalCustomColors.current.bodyContentColor
            )
        }
        IconButton(onClick = { /* Delete */ }) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Type",
                tint = LocalCustomColors.current.bodyContentColor
            )
        }
        IconButton(onClick = { /* Type */ }) {
            Icon(
                imageVector = Images.Icons.IcDetailType,
                contentDescription = "Type",
                tint = LocalCustomColors.current.bodyContentColor
            )
        }
        IconButton(onClick = { /* Undo */ }) {
            Icon(
                imageVector = Images.Icons.IcDetailUndo,
                contentDescription = "Undo",
                tint = LocalCustomColors.current.bodyContentColor
            )
        }
        IconButton(onClick = { /* Redo */ }) {
            Icon(
                imageVector = Images.Icons.IcDetailRedo,
                contentDescription = "Redo",
                tint = LocalCustomColors.current.bodyContentColor
            )
        }
    }
}

@Composable
fun EditingToolbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = Color.Blue,
                shape = RoundedCornerShape(16.dp) // Adjust the radius as needed
            )
            .clip(RoundedCornerShape(16.dp)), // Ensures the content respects the rounded corners
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Handle B click */ }) {
            Icon(
                imageVector = Images.Icons.IcDetailBold,
                contentDescription = "Bold",
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { /* Handle I click */ }) {
            Icon(
                imageVector = Images.Icons.IcDetailItalic,
                contentDescription = "Italic",
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { /* Handle U click */ }) {
            Icon(
                imageVector = Images.Icons.IcDetailUnderline,
                contentDescription = "Underline",
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { /* Handle List click */ }) {
            Icon(
                imageVector = Images.Icons.IcDetailList,
                contentDescription = "List",
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { /* Handle Align Center click */ }) {
            Icon(
                imageVector = Images.Icons.IcDetailAlignLeft,
                contentDescription = "Align Center",
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { /* Handle Align Right click */ }) {
            Icon(
                imageVector = Images.Icons.IcDetailAlignCenter,
                contentDescription = "Align Right",
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { /* Handle Align Right click */ }) {
            Icon(
                imageVector = Images.Icons.IcDetailAlignRight,
                contentDescription = "Align Right",
                tint = Color.LightGray
            )
        }
    }
}
