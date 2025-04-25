package com.module.notelycompose.notes.ui.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import notelycompose.shared.generated.resources.Res
import notelycompose.shared.generated.resources.note_item_starred
import notelycompose.shared.generated.resources.note_item_voice
import notelycompose.shared.generated.resources.note_item_note
import org.jetbrains.compose.resources.stringResource

@Composable
fun NoteType(
    isStarred: Boolean,
    isVoice: Boolean
) {
    when {
        isStarred && isVoice -> {
            Row {
                NoteTypeCard(stringResource(Res.string.note_item_starred))
                Spacer(modifier = Modifier.width(8.dp))
                NoteTypeCard(stringResource(Res.string.note_item_voice))
            }
        }
        isStarred -> {
            NoteTypeCard(stringResource(Res.string.note_item_starred))
        }
        isVoice -> {
            NoteTypeCard(stringResource(Res.string.note_item_voice))
        }
        else -> {
            NoteTypeCard(stringResource(Res.string.note_item_note))
        }
    }
}

@Composable
private fun NoteTypeCard(text: String) {
    Card(
        modifier = Modifier.padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFFD18B60).copy(alpha = 0.5f)
    ) {
        Text(
            text = text,
            color = LocalCustomColors.current.noteTextColor,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)

        )
    }
}
