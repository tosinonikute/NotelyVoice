package com.module.notelycompose.notes.presentation.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.notes.presentation.list.ui.TopBar

@Composable
fun NoteDetailScreen(
    title: String? = null,
    content: String? = null,
    onSaveClick: (String, String, Boolean) -> Unit
) {
    val titleState = remember { mutableStateOf(title) }
    val contentState = remember { mutableStateOf(content) }
    val isUpdateScreen = remember { mutableStateOf(title != null) }

    LaunchedEffect(title) {
        titleState.value = title
    }
    Scaffold(
        topBar = {
            TopBar(titleState.value ?: "Note", false, false)
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                onClick = {
                    if (titleState.value != null && contentState.value != null) {
                        onSaveClick(titleState.value!!, contentState.value!!, isUpdateScreen.value)
                    }
                },
                backgroundColor = Color.Black,
            ) {
                Text("Save", color = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TransparentHintTextField(
                labelName = "Title",
                text = titleState.value ?: "",
                hint = "",
                isHintVisible = titleState.value == null,
                onValueChanged = {
                    titleState.value = it
                },
                onFocusChanged = {

                },
                singleLine = true,
                textStyle = TextStyle(fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                labelName = "Content",
                text = contentState.value ?: "",
                hint = "",
                isHintVisible = contentState.value == null,
                onValueChanged = {
                    contentState.value = it
                },
                onFocusChanged = {

                },
                singleLine = false,
                textStyle = TextStyle(fontSize = 20.sp),
                modifier = Modifier.weight(1f)
            )
        }
    }
}