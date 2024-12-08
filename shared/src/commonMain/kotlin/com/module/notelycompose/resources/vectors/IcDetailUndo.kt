package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector
import androidx.compose.ui.graphics.StrokeCap.Companion.Round as strokeCapRound
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round as strokeJoinRound

public val Images.Icons.IcDetailUndo: ImageVector
    get() {
        if (_icDetailUndo != null) {
            return _icDetailUndo!!
        }
        _icDetailUndo = imageVector(
        	name = "IcDetailUndo",
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
            	strokeLineWidth = 1.5f
            ) {
                moveTo(4.0f, 9.0f)
                verticalLineTo(15.0f)
                moveTo(4.0f, 15.0f)
                horizontalLineTo(10.0f)
                moveTo(4.0f, 15.0f)
                curveTo(6.327f, 12.911f, 8.483f, 10.547f, 11.745f, 10.088f)
                curveTo(13.678f, 9.816f, 15.646f, 10.179f, 17.354f, 11.123f)
                curveTo(19.062f, 12.068f, 20.416f, 13.541f, 21.214f, 15.322f)
            }
        }
        return _icDetailUndo!!
    }

private var _icDetailUndo: ImageVector? = null
