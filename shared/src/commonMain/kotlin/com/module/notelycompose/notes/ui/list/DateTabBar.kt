package com.module.notelycompose.notes.ui.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.module.notelycompose.resources.vectors.IcFile
import com.module.notelycompose.resources.vectors.IcFolder
import com.module.notelycompose.resources.vectors.IcStar
import com.module.notelycompose.resources.vectors.IcRecorderSmall
import com.module.notelycompose.resources.vectors.Images

@Composable
fun DateTabBar() {
    val titles = listOf("All", "Recent", "Starred", "Voices")
    val selectedTitle = remember { mutableStateOf(titles[0]) }

    val icons = listOf(
        Images.Icons.IcFile,
        Images.Icons.IcFolder,
        Images.Icons.IcStar,
        Images.Icons.IcRecorderSmall
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
        DateSelection(
            titles = titles,
            icons = icons,
            tabSelected = selectedTitle.value,
            onTabSelected = { title ->
                selectedTitle.value = title
            }
        )
    }
}
