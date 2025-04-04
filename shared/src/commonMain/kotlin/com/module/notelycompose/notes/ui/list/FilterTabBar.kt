package com.module.notelycompose.notes.ui.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.module.notelycompose.audio.ui.keepFirstCharCaseExt
import com.module.notelycompose.resources.vectors.IcFile
import com.module.notelycompose.resources.vectors.IcFolder
import com.module.notelycompose.resources.vectors.IcStar
import com.module.notelycompose.resources.vectors.IcRecorderSmall
import com.module.notelycompose.resources.vectors.Images
import notelycompose.shared.generated.resources.Res
import notelycompose.shared.generated.resources.date_tab_bar_all
import notelycompose.shared.generated.resources.date_tab_bar_starred
import notelycompose.shared.generated.resources.date_tab_bar_voices
import notelycompose.shared.generated.resources.date_tab_bar_recent
import org.jetbrains.compose.resources.stringResource

@Composable
fun FilterTabBar(
    onFilterTabItemClicked: (String) -> Unit
) {
    val titles = listOf(
        stringResource(Res.string.date_tab_bar_all),
        stringResource(Res.string.date_tab_bar_starred),
        stringResource(Res.string.date_tab_bar_voices),
        stringResource(Res.string.date_tab_bar_recent)
    )
    val selectedTitle = remember { mutableStateOf(titles[0]) }

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
            tabSelected = selectedTitle.value,
            onTabSelected = { title ->
                selectedTitle.value = title
                onFilterTabItemClicked(title)
            }
        )
    }
}
