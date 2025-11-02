package com.module.notelycompose.notes.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.notes.ui.detail.AndroidNoteTopBar
import com.module.notelycompose.notes.ui.detail.IOSNoteTopBar
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.platform.getPlatform
import com.module.notelycompose.resources.Res
import com.module.notelycompose.resources.ai_model_description
import com.module.notelycompose.resources.ai_model_title
import com.module.notelycompose.resources.optimized_model_description
import com.module.notelycompose.resources.optimized_model_title
import com.module.notelycompose.resources.standard_model_description
import com.module.notelycompose.resources.standard_model_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun ModelExplanationScreen(
    navigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.bodyBackgroundColor)
    ) {
        if (getPlatform().isAndroid) {
            AndroidNoteTopBar(
                title = "",
                onNavigateBack = navigateBack
            )
        } else {
            IOSNoteTopBar(
                onNavigateBack = navigateBack
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {

            // Subtitle
            Text(
                text = stringResource(Res.string.ai_model_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = LocalCustomColors.current.bodyContentColor
            )

            Text(
                text = stringResource(Res.string.ai_model_description),
                fontSize = 16.sp,
                color = LocalCustomColors.current.modelSelectionDescColor,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = stringResource(Res.string.standard_model_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = LocalCustomColors.current.bodyContentColor
            )

            Text(
                text = stringResource(Res.string.standard_model_description),
                fontSize = 16.sp,
                color = LocalCustomColors.current.modelSelectionDescColor,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = stringResource(Res.string.optimized_model_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = LocalCustomColors.current.bodyContentColor
            )

            Text(
                text = stringResource(Res.string.optimized_model_description),
                fontSize = 16.sp,
                color = LocalCustomColors.current.modelSelectionDescColor,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
        // end of content
    }
}
