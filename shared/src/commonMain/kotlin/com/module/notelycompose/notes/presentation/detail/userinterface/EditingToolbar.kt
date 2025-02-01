package com.module.notelycompose.notes.presentation.detail.userinterface

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.module.notelycompose.resources.vectors.IcDetailAlignCenter
import com.module.notelycompose.resources.vectors.IcDetailAlignLeft
import com.module.notelycompose.resources.vectors.IcDetailAlignRight
import com.module.notelycompose.resources.vectors.IcDetailBold
import com.module.notelycompose.resources.vectors.IcDetailItalic
import com.module.notelycompose.resources.vectors.IcDetailUnderline
import com.module.notelycompose.resources.vectors.Images

@Composable
fun EditingToolbar(
    onToggleBold: () -> Unit,
    onToggleItalic: () -> Unit,
    onToggleUnderline: () -> Unit,
    onSetAlignment: (alignment: TextAlign) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 8.dp)
            .background(
                color = Color.Blue,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onToggleBold() }) {
            Icon(
                imageVector = Images.Icons.IcDetailBold,
                contentDescription = "Bold",
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { onToggleItalic() }) {
            Icon(
                imageVector = Images.Icons.IcDetailItalic,
                contentDescription = "Italic",
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { onToggleUnderline() }) {
            Icon(
                imageVector = Images.Icons.IcDetailUnderline,
                contentDescription = "Underline",
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { onSetAlignment(TextAlign.Left) }) {
            Icon(
                imageVector = Images.Icons.IcDetailAlignLeft,
                contentDescription = "Align Center",
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { onSetAlignment(TextAlign.Center) }) {
            Icon(
                imageVector = Images.Icons.IcDetailAlignCenter,
                contentDescription = "Align Right",
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { onSetAlignment(TextAlign.Right) }) {
            Icon(
                imageVector = Images.Icons.IcDetailAlignRight,
                contentDescription = "Align Right",
                tint = Color.LightGray
            )
        }
    }
}

