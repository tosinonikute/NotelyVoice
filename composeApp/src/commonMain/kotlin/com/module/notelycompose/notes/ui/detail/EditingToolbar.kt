package com.module.notelycompose.notes.ui.detail

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
import notelycompose.composeapp.generated.resources.Res
import notelycompose.composeapp.generated.resources.editing_bold
import notelycompose.composeapp.generated.resources.editing_italic
import notelycompose.composeapp.generated.resources.editing_underline
import notelycompose.composeapp.generated.resources.editing_align_left
import notelycompose.composeapp.generated.resources.editing_align_center
import notelycompose.composeapp.generated.resources.editing_align_right
import org.jetbrains.compose.resources.stringResource

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
                contentDescription = stringResource(Res.string.editing_bold),
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { onToggleItalic() }) {
            Icon(
                imageVector = Images.Icons.IcDetailItalic,
                contentDescription = stringResource(Res.string.editing_italic),
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { onToggleUnderline() }) {
            Icon(
                imageVector = Images.Icons.IcDetailUnderline,
                contentDescription = stringResource(Res.string.editing_underline),
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { onSetAlignment(TextAlign.Left) }) {
            Icon(
                imageVector = Images.Icons.IcDetailAlignLeft,
                contentDescription = stringResource(Res.string.editing_align_left),
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { onSetAlignment(TextAlign.Center) }) {
            Icon(
                imageVector = Images.Icons.IcDetailAlignCenter,
                contentDescription = stringResource(Res.string.editing_align_center),
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { onSetAlignment(TextAlign.Right) }) {
            Icon(
                imageVector = Images.Icons.IcDetailAlignRight,
                contentDescription = stringResource(Res.string.editing_align_right),
                tint = Color.LightGray
            )
        }
    }
}

