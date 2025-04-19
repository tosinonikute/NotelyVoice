package com.module.notelycompose.notes.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import notelycompose.shared.generated.resources.Res
import notelycompose.shared.generated.resources.format_bar_text
import notelycompose.shared.generated.resources.format_bar_close
import org.jetbrains.compose.resources.stringResource

@Composable
fun FormatBar(
    modifier: Modifier = Modifier,
    selectedFormat: FormatOptionTextFormat = FormatOptionTextFormat.Body,
    onFormatSelected: (FormatOptionTextFormat) -> Unit,
    onClose: () -> Unit,
    onToggleBold: () -> Unit,
    onToggleItalic: () -> Unit,
    onToggleUnderline: () -> Unit,
    onSetAlignment: (alignment: TextAlign) -> Unit
) {
    Surface(
        modifier = modifier,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
        color = LocalCustomColors.current.bottomFormattingContainerColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.format_bar_text),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = LocalCustomColors.current.bottomFormattingContentColor
                )
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(Res.string.format_bar_close),
                        tint = LocalCustomColors.current.bottomFormattingContentColor
                    )
                }
            }

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FormatOptionTextFormat.values().forEach { format ->
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EditingToolbar(
                    onToggleBold = { onToggleBold() },
                    onToggleItalic = { onToggleItalic() },
                    onToggleUnderline = { onToggleUnderline() },
                    onSetAlignment = { alignment ->
                        onSetAlignment(alignment)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FormatOption(
    format: FormatOptionTextFormat,
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
            FormatOptionTextFormat.Title -> 20.sp
            FormatOptionTextFormat.Heading -> 18.sp
            FormatOptionTextFormat.Subheading -> 16.sp
            FormatOptionTextFormat.Body -> 14.sp
        },
        fontWeight = when (format) {
            FormatOptionTextFormat.Title -> FontWeight.Bold
            FormatOptionTextFormat.Heading -> FontWeight.SemiBold
            FormatOptionTextFormat.Subheading -> FontWeight.Medium
            FormatOptionTextFormat.Body -> FontWeight.Normal
        },
        color = if (isSelected) {
            Color.White
        } else {
            Color.Black
        }
    )
}

enum class FormatOptionTextFormat(val title: String) {
    Title("Title"),
    Heading("Heading"),
    Subheading("Subheading"),
    Body("Body")
}