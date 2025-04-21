package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector
import androidx.compose.ui.graphics.StrokeCap.Companion.Round as strokeCapRound
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round as strokeJoinRound

public val Images.Icons.IcDetailList: ImageVector
    get() {
        if (_icDetailList != null) {
            return _icDetailList!!
        }
        _icDetailList = imageVector(
        	name = "IcDetailList",
        	width = 32f,
        	height = 32f,
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
                moveTo(7.0f, 8.0f)
                horizontalLineTo(21.0f)
                moveTo(7.0f, 12.0f)
                horizontalLineTo(21.0f)
                moveTo(7.0f, 16.0f)
                horizontalLineTo(21.0f)
                moveTo(3.0f, 8.0f)
                horizontalLineTo(3.01f)
                moveTo(3.0f, 12.0f)
                horizontalLineTo(3.01f)
                moveTo(3.0f, 16.0f)
                horizontalLineTo(3.01f)
            }
        }
        return _icDetailList!!
    }

private var _icDetailList: ImageVector? = null
