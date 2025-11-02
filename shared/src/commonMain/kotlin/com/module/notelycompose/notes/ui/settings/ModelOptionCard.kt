package com.module.notelycompose.notes.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.notes.ui.theme.LocalCustomColors

@Composable
fun ModelOptionCard(
    model: ModelOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                LocalCustomColors.current.modelSelectionBgColor // Light blue background when selected
            } else {
                LocalCustomColors.current.modelSelectionBgColor // Light gray background when not selected
            }
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF2196F3)) // Blue border when selected
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)) // Gray border when not selected
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = model.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LocalCustomColors.current.bodyContentColor,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = model.description,
                    fontSize = 14.sp,
                    color = LocalCustomColors.current.modelSelectionDescColor,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = model.size,
                    fontSize = 14.sp,
                    color = LocalCustomColors.current.modelSelectionDescColor
                )
            }

            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF2196F3),
                    unselectedColor = Color.Gray
                )
            )
        }
    }
}
