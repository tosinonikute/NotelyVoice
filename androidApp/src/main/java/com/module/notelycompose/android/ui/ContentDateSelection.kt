package com.module.notelycompose.android.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun ContentDateSelection(
    titles: List<String>,
    icons: List<Painter>,
    tabSelected: String,
    onTabSelected: (String) -> Unit
) {
    ScrollableTabRow(
        edgePadding = 0.dp,
        backgroundColor = Color.Transparent,
        selectedTabIndex = titles.indexOf(tabSelected),
        contentColor = Color.Black,
        indicator = { tabPositions: List<TabPosition> ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[titles.indexOf(tabSelected)])
                    .fillMaxSize()
                    .padding(horizontal = 4.dp)
                    .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(16.dp))
            )
        },
        divider = { }
    ) {
        titles.forEachIndexed { index, title ->
            val selected = index == titles.indexOf(tabSelected)
            val icon = icons[index]

            val textModifier = Modifier
                .padding(vertical = 4.dp, horizontal = 4.dp)

            Tab(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clip(RoundedCornerShape(16.dp)),
                selected = selected,
                onClick = {
                    onTabSelected(titles[index])
                }
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 6.dp)
                ) {
                    Icon(
                        painter = icon,
                        contentDescription = "More options",
                        tint = Color.Black
                    )
                    Text(
                        modifier = textModifier,
                        text = title,
                    )
                }
            }
        }
    }
}
