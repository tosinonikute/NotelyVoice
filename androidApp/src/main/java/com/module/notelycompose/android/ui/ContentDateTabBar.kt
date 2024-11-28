package com.module.notelycompose.android.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.module.notelycompose.android.R

@Composable
fun ContentDateTabBar() {
    val titles = listOf("All", "Recent", "Starred", "Voices")
    val selectedTitle = remember { mutableStateOf(titles[0]) }
    val icons = listOf(
        painterResource(id = R.drawable.ic_file),
        painterResource(id = R.drawable.ic_folder),
        painterResource(id = R.drawable.ic_star),
        painterResource(id = R.drawable.ic_heart)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 8.dp,
                bottom = 8.dp
            )
    ) {
        ContentDateSelection(
            titles = titles,
            icons = icons,
            tabSelected = selectedTitle.value,
            onTabSelected = { title ->
                selectedTitle.value = title
            }
        )
    }
}
