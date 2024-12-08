package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector
import androidx.compose.ui.graphics.StrokeCap.Companion.Round as strokeCapRound
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round as strokeJoinRound

public val Images.Icons.IcDetailItalic: ImageVector
    get() {
        if (_icDetailItalic != null) {
            return _icDetailItalic!!
        }
        _icDetailItalic = imageVector(
        	name = "IcDetailItalic",
        	width = 24f,
        	height = 24f,
        	viewportWidth = 24.0f,
        	viewportHeight = 24.0f,
        	autoMirror = false
        ) {
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF000000)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 2.0f
            ) {
                moveTo(19.0f, 4.0f)
                horizontalLineTo(10.0f)
                moveTo(14.0f, 20.0f)
                horizontalLineTo(5.0f)
                moveTo(15.0f, 4.0f)
                lineTo(9.0f, 20.0f)
            }
        }
        return _icDetailItalic!!
    }

private var _icDetailItalic: ImageVector? = null
