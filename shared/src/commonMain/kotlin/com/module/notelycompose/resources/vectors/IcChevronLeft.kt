package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector
import androidx.compose.ui.graphics.StrokeCap.Companion.Round as strokeCapRound
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round as strokeJoinRound

public val Images.Icons.IcChevronLeft: ImageVector
    get() {
        if (_icChevronLeft != null) {
            return _icChevronLeft!!
        }
        _icChevronLeft = imageVector(
            name = "IcChevronLeft",
            width = 800f,
            height = 800f,
            viewportWidth = 20.0f,
            viewportHeight = 20.0f,
            autoMirror = false
        ) {
            path(
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineCap = strokeCapRound,
                strokeLineJoin = strokeJoinRound,
                strokeLineWidth = 2.0f
            ) {
                moveTo(13.0f, 4.0f)
                lineToRelative(-6.0f, 6.0f)
                lineToRelative(6.0f, 6.0f)
            }
        }
        return _icChevronLeft!!
    }

private var _icChevronLeft: ImageVector? = null
