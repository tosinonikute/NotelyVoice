package com.module.notelycompose.notes.ui.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.module.notelycompose.resources.vectors.IcFile
import com.module.notelycompose.resources.vectors.IcFolder
import com.module.notelycompose.resources.vectors.IcStar
import com.module.notelycompose.resources.vectors.IcRecorderSmall
import com.module.notelycompose.resources.vectors.Images
import com.module.notelycompose.resources.Res
import com.module.notelycompose.resources.date_tab_bar_all
import com.module.notelycompose.resources.date_tab_bar_starred
import com.module.notelycompose.resources.date_tab_bar_voices
import com.module.notelycompose.resources.date_tab_bar_recent
import org.jetbrains.compose.resources.stringResource

@Composable
fun FilterTabBar(
    onFilterTabItemClicked: (Int) -> Unit,
    selectedTabIndex: Int,
    allSizeStr: String
) {
    val tabResources = listOf(
        Res.string.date_tab_bar_all,
        Res.string.date_tab_bar_starred,
        Res.string.date_tab_bar_voices,
        Res.string.date_tab_bar_recent
    )
    val titles = tabResources.map {
        when(it) {
            Res.string.date_tab_bar_all -> stringResource(it, allSizeStr)
            else -> stringResource(it)
        }
    }

    val selectedTitle = titles[selectedTabIndex].ifEmpty {
        titles[0]
    }

    val icons = listOf(
        Images.Icons.IcFile,
        Images.Icons.IcStar,
        Images.Icons.IcRecorderSmall,
        Images.Icons.IcFolder
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
        FilterSelection(
            titles = titles,
            icons = icons,
            tabSelected = selectedTitle,
            onTabSelected = { title ->
                val onTabSelectedTabIndex = titles.indexOf(title)
                onFilterTabItemClicked(onTabSelectedTabIndex)
            }
        )
    }
}
