package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector
import androidx.compose.ui.graphics.StrokeCap.Companion.Round as strokeCapRound
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round as strokeJoinRound

public val Images.Icons.IcArrowUpRight: ImageVector
    get() {
        if (_icArrowUpRight != null) {
            return _icArrowUpRight!!
        }
        _icArrowUpRight = imageVector(
        	name = "IcArrowUpRight",
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
                moveTo(7.0f, 17.0f)
                lineTo(17.0f, 7.0f)
                moveTo(17.0f, 7.0f)
                horizontalLineTo(8.0f)
                moveTo(17.0f, 7.0f)
                verticalLineTo(16.0f)
            }
        }
        return _icArrowUpRight!!
    }

private var _icArrowUpRight: ImageVector? = null
