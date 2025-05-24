package com.module.notelycompose.notes.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.getPlatform
import com.module.notelycompose.notes.ui.detail.AndroidNoteTopBar
import com.module.notelycompose.notes.ui.detail.IOSNoteTopBar
import com.module.notelycompose.notes.ui.theme.LocalCustomColors

data class Language(
    val name: String
)

@Composable
fun LanguageSelectionScreen(
    onBackPressed: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    val languages = listOf(
        Language("English"),
        Language("English (Canada)"),
        Language("English (UK)"),
        Language("English (Australia)"),
        Language("English (America)")
    )

    val languages2 = listOf(
        Language("French"),
        Language("French (Canada)"),
        Language("French (Burkina Faso)")
    )

    if (getPlatform().isAndroid) {
        AndroidNoteTopBar(
            title = "",
            onNavigateBack = onBackPressed
        )
    } else {
        IOSNoteTopBar(
            onNavigateBack = onBackPressed
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.bodyBackgroundColor)
            .padding(16.dp)
    ) {

        // Title
        Text(
            text = "Select Language",
            color = LocalCustomColors.current.bodyContentColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            placeholder = {
                Text(
                    text = "Search",
                    color = LocalCustomColors.current.languageSearchBorderColor
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = LocalCustomColors.current.languageSearchBorderColor
                )
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(
                        onClick = { searchText = "" },
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                LocalCustomColors.current.languageSearchCancelButtonColor.copy(alpha = 0.3f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = LocalCustomColors.current.languageSearchCancelIconTintColor,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = LocalCustomColors.current.languageSearchUnfocusedColor,
                unfocusedTextColor = LocalCustomColors.current.languageSearchUnfocusedColor,
                focusedBorderColor = LocalCustomColors.current.languageSearchBorderColor,
                unfocusedBorderColor = LocalCustomColors.current.languageSearchBorderColor,
                cursorColor = LocalCustomColors.current.languageSearchUnfocusedColor
            ),
            shape = RoundedCornerShape(48.dp),
            singleLine = true
        )

        // Language List

        Text(
            text = "SUPPORTED LANGUAGES",
            color = LocalCustomColors.current.languageListHeaderColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp)
        )

        // start

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(languages) { language ->
                // Check if this is the first or last language item
                val isFirst = language == languages.first()
                val isLast = language == languages.last()

                val shape = when {
                    isFirst && isLast -> RoundedCornerShape(12.dp)
                    isFirst -> RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                    isLast -> RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                    else -> RoundedCornerShape(0.dp)
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .clickable { /* Handle language selection */ },
                    color = LocalCustomColors.current.languageListBackgroundColor,
                    shape = shape
                ) {
                    Column {
                        Text(
                            text = language.name,
                            color = LocalCustomColors.current.languageListTextColor,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )

                        // Add separator line between items (except for the last item)
                        if (!isLast) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(0.5.dp)
                                    .background(LocalCustomColors.current.languageListDividerColor.copy(alpha = 0.3f))
                                    .padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }

        // end

        Spacer(modifier = Modifier.padding(top = 16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(languages2) { language ->
                // Check if this is the first or last language item
                val isFirst = language == languages2.first()
                val isLast = language == languages2.last()

                val shape = when {
                    isFirst && isLast -> RoundedCornerShape(12.dp)
                    isFirst -> RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                    isLast -> RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                    else -> RoundedCornerShape(0.dp)
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .clickable { /* Handle language selection */ },
                    color = LocalCustomColors.current.languageListBackgroundColor,
                    shape = shape
                ) {
                    Column {
                        Text(
                            text = language.name,
                            color = LocalCustomColors.current.languageListTextColor,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )

                        // Add separator line between items (except for the last item)
                        if (!isLast) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(0.5.dp)
                                    .background(LocalCustomColors.current.languageListDividerColor.copy(alpha = 0.3f))
                                    .padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
