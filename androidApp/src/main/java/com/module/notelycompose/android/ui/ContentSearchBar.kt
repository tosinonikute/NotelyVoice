package com.module.notelycompose.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.module.notelycompose.android.R
import com.module.notelycompose.android.theme.LocalCustomColors
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun ContentSearchBar() {
    var value by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    var isLabelVisible by remember { mutableStateOf(true) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                value = it
                isLabelVisible = !isFocused && value.isEmpty()
            },
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    isLabelVisible = !isFocused && value.isEmpty()
                },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Color.White,
                textColor = Color.White,
                focusedBorderColor = Color.White, // Color when focused
                unfocusedBorderColor = Color.White, // Color when not focused
                disabledBorderColor = Color.White // Color when disabled
            ),
            label = {
                if (isLabelVisible) {
                    Text(
                        text = "Search notes...",
                        color = Color.White
                    )
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}