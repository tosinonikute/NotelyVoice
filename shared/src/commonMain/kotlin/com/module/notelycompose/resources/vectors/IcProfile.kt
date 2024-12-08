package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector
import androidx.compose.ui.graphics.StrokeCap.Companion.Round as strokeCapRound
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round as strokeJoinRound

public val Images.Icons.IcProfile: ImageVector
    get() {
        if (_icProfile != null) {
            return _icProfile!!
        }
        _icProfile = imageVector(
        	name = "IcProfile",
        	width = 24f,
        	height = 24f,
        	viewportWidth = 24.0f,
        	viewportHeight = 24.0f,
        	autoMirror = false
        ) {
            path(
            	fill = SolidColor(Color(0xFF000000))
            ) {
                moveTo(15.0f, 8.5f)
                curveTo(15.0f, 10.433f, 13.433f, 12.0f, 11.5f, 12.0f)
                curveTo(9.567f, 12.0f, 8.0f, 10.433f, 8.0f, 8.5f)
                curveTo(8.0f, 6.567f, 9.567f, 5.0f, 11.5f, 5.0f)
                curveTo(13.433f, 5.0f, 15.0f, 6.567f, 15.0f, 8.5f)
                close()
            }
            path(
            	fill = SolidColor(Color(0xFF000000)),
            	stroke = SolidColor(Color(0xFF000000)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 2.0f
            ) {
                moveTo(17.631f, 20.0f)
                horizontalLineTo(5.946f)
                curveTo(5.544f, 20.0f, 5.317f, 19.559f, 5.568f, 19.245f)
                curveTo(6.684f, 17.848f, 9.291f, 15.0f, 12.0f, 15.0f)
                curveTo(14.727f, 15.0f, 17.063f, 17.886f, 18.027f, 19.273f)
                curveTo(18.247f, 19.59f, 18.016f, 20.0f, 17.631f, 20.0f)
                close()
            }
        }
        return _icProfile!!
    }

private var _icProfile: ImageVector? = null
