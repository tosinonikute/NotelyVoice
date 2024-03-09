package com.module.notelycompose.notes.presentation.list.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DateSelection(
    titles: List<String>,
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

            val textModifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)

            Tab(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clip(RoundedCornerShape(16.dp)),
                selected = selected,
                onClick = {
                    onTabSelected(titles[index])
                }
            ) {
                Text(
                    modifier = textModifier,
                    text = title.uppercase(),
                )
            }
        }
    }
}