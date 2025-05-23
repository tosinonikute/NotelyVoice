package com.module.notelycompose.notes.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.notes.ui.theme.LocalCustomColors

@Composable
fun SettingsScreen(
    onDismiss: () -> Unit
) {
    var selectedTheme by remember { mutableStateOf(Theme.SYSTEM) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.bodyBackgroundColor)
    ) {
        // Header
        SettingsHeader(
            onDismiss = onDismiss
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            item {
                LanguageRegionSection()
            }

            item {
                AppearanceSection(
                    selectedTheme = selectedTheme,
                    onThemeSelected = { selectedTheme = it }
                )
            }
        }
    }
}

@Composable
private fun SettingsHeader(
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Settings",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = LocalCustomColors.current.bodyContentColor
            )
            Text(
                text = "Customize your experience",
                fontSize = 16.sp,
                color = LocalCustomColors.current.bodyContentColor,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        IconButton(
            onClick = { onDismiss() },
            modifier = Modifier
                .size(48.dp)
                .background(
                    LocalCustomColors.current.settingCancelBackgroundColor,
                    RoundedCornerShape(24.dp)
                )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = LocalCustomColors.current.settingCancelTextColor
            )
        }
    }
}

@Composable
private fun LanguageRegionSection() {
    Column {
        Text(
            text = "Language & Region",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = LocalCustomColors.current.bodyContentColor,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        TranscriptionLanguageItem()
    }
}

@Composable
private fun TranscriptionLanguageItem() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Transcription Language",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = LocalCustomColors.current.bodyContentColor,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "Language used for voice transcription",
            fontSize = 14.sp,
            color = LocalCustomColors.current.bodyContentColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Handle language selection */ }
                .border(
                    2.dp,
                    LocalCustomColors.current.bodyContentColor,
                    RoundedCornerShape(8.dp)
            ),
            colors = CardDefaults.cardColors(
                containerColor = LocalCustomColors.current.settingLanguageBackgroundColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                Color(0xFF6366F1),
                                RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "EN",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "English",
                        fontSize = 16.sp,
                        color = LocalCustomColors.current.bodyContentColor,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Select language",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun AppearanceSection(
    selectedTheme: Theme,
    onThemeSelected: (Theme) -> Unit
) {
    Column {
        Text(
            text = "Appearance",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = LocalCustomColors.current.bodyContentColor,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        ThemeSection(
            selectedTheme = selectedTheme,
            onThemeSelected = onThemeSelected
        )
    }
}

@Composable
private fun ThemeSection(
    selectedTheme: Theme,
    onThemeSelected: (Theme) -> Unit
) {
    Column {
        Text(
            text = "Theme",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = LocalCustomColors.current.bodyContentColor,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "Choose how the app looks and feels",
            fontSize = 14.sp,
            color = LocalCustomColors.current.bodyContentColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ThemeOption(
                theme = Theme.LIGHT,
                isSelected = selectedTheme == Theme.LIGHT,
                onSelected = { onThemeSelected(Theme.LIGHT) },
                modifier = Modifier.weight(1f)
            )
            ThemeOption(
                theme = Theme.DARK,
                isSelected = selectedTheme == Theme.DARK,
                onSelected = { onThemeSelected(Theme.DARK) },
                modifier = Modifier.weight(1f)
            )
            ThemeOption(
                theme = Theme.SYSTEM,
                isSelected = selectedTheme == Theme.SYSTEM,
                onSelected = { onThemeSelected(Theme.SYSTEM) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ThemeOption(
    theme: Theme,
    isSelected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onSelected() }
            .then(
                if (isSelected) {
                    Modifier.border(
                        2.dp,
                        LocalCustomColors.current.bodyContentColor,
                        RoundedCornerShape(12.dp)
                    )
                } else {
                    Modifier.border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        RoundedCornerShape(12.dp)
                    )
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = LocalCustomColors.current.bodyBackgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ThemePreview(theme = theme)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = theme.displayName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = LocalCustomColors.current.bodyContentColor
            )
        }
    }
}

@Composable
private fun ThemePreview(theme: Theme) {
    when (theme) {
        Theme.LIGHT -> {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color(0xFFF1F1F1),
                        RoundedCornerShape(8.dp)
                    )
            )
        }
        Theme.DARK -> {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color(0xFF2D3748),
                        RoundedCornerShape(8.dp)
                    )
            )
        }
        Theme.SYSTEM -> {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                // Left half - light
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f)
                        .background(Color(0xFFF1F1F1))
                        .align(Alignment.CenterStart)
                )
                // Right half - dark
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f)
                        .background(Color(0xFF2D3748))
                        .align(Alignment.CenterEnd)
                )
            }
        }
        else -> {
            // Fallback for any other themes
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color(0xFFF1F1F1),
                        RoundedCornerShape(8.dp)
                    )
            )
        }
    }
}

enum class Theme(val displayName: String) {
    LIGHT("Light"),
    DARK("Dark"),
    SYSTEM("System")
}
