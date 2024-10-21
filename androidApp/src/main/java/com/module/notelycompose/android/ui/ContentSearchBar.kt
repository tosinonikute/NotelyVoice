package com.module.notelycompose.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
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
            },
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    if (focusState.isFocused) {
                        isLabelVisible = false
                    } else if (value.isEmpty()) {
                        isLabelVisible = true
                    }
                },
            label = {
                if (isLabelVisible) {
                    Text(text = "Search notes...")
                }
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            shape = RoundedCornerShape(16.dp)
        )
        IconButton(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(LocalCustomColors.current.sortAscendingIconColor)
                //.padding(4.dp)
            ,
            onClick = { /* TODO */ }) {
            Icon(
                painter = painterResource(id = R.drawable.sort_descending),
                contentDescription = "More options",
                tint = Color.Black
            )
        }
    }
}