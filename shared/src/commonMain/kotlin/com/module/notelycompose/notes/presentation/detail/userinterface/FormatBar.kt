package com.module.notelycompose.notes.presentation.detail.userinterface

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.notes.presentation.theme.LocalCustomColors

@Composable
fun FormatBar(
    modifier: Modifier = Modifier,
    selectedFormat: TextFormat = TextFormat.Body,
    onFormatSelected: (TextFormat) -> Unit,
    onClose: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = LocalCustomColors.current.bottomFormattingContainerColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Format",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = LocalCustomColors.current.bottomFormattingContentColor
                )
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = LocalCustomColors.current.bottomFormattingContentColor
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextFormat.values().forEach { format ->
                    FormatOption(
                        format = format,
                        isSelected = format == selectedFormat,
                        onClick = { onFormatSelected(format) }
                    )
                }
                // Add end padding for last item
                Spacer(modifier = Modifier.width(4.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EditingToolbar()
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FormatOption(
    format: TextFormat,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = format.title,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) Color(0xFFE4B441)
                else Color.Transparent
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        fontSize = when (format) {
            TextFormat.Title -> 20.sp
            TextFormat.Heading -> 18.sp
            TextFormat.Subheading -> 16.sp
            TextFormat.Body -> 14.sp
        },
        fontWeight = when (format) {
            TextFormat.Title -> FontWeight.Bold
            TextFormat.Heading -> FontWeight.SemiBold
            TextFormat.Subheading -> FontWeight.Medium
            TextFormat.Body -> FontWeight.Normal
        },
        color = if (isSelected) {
            Color.White
        } else {
            Color.Black
        }
    )
}

enum class TextFormat(val title: String) {
    Title("Title"),
    Heading("Heading"),
    Subheading("Subheading"),
    Body("Body")
}