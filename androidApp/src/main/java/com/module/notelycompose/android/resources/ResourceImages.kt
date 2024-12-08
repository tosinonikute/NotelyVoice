package com.module.notelycompose.android.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.module.notelycompose.android.R
import com.module.notelycompose.resources.ResourceImages
import androidx.compose.ui.res.painterResource

val ResourceImages?.resId: Painter
    @Composable
    get() {
        return when (this) {
            null -> painterResource(R.drawable.ic_file)
            ResourceImages.IC_FILE -> painterResource(R.drawable.ic_file)
        }
    }
