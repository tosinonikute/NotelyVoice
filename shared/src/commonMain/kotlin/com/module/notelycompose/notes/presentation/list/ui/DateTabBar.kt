package com.module.notelycompose.notes.presentation.list.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DateTabBar() {
    val titles = listOf("Today", "Yesterday", "Week", "Month")
    val selectedTitle = remember { mutableStateOf(titles[0]) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 24.dp,
                bottom = 8.dp
            )
    ) {
        DateSelection(
            titles = titles,
            tabSelected = selectedTitle.value,
            onTabSelected = { title ->
                selectedTitle.value = title
            }
        )
    }
}