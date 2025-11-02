package com.module.notelycompose.export.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.resources.Res
import com.module.notelycompose.resources.cancel
import com.module.notelycompose.resources.ic_cancel_all
import com.module.notelycompose.resources.select_deselect_all
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SelectAllToExportUi(
    onSelectAllChecked: (Boolean) -> Unit,
    isSelectAllAction: Boolean,
    onCancelSelectionAction: () -> Unit,
) {
    AnimatedVisibility(
        visible = isSelectAllAction,
        enter = expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(),
        exit = shrinkVertically(
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        var isChecked by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    onSelectAllChecked(isChecked)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = LocalCustomColors.current.selectAllCheckboxColor
                )
            )

            Text(
                text = stringResource(Res.string.select_deselect_all),
                color = LocalCustomColors.current.bodyContentColor
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = {
                onCancelSelectionAction()
            }) {
                Icon(
                    painter = painterResource(Res.drawable.ic_cancel_all),
                    contentDescription = stringResource(Res.string.cancel),
                    tint = LocalCustomColors.current.languageSearchCancelButtonColor,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}
