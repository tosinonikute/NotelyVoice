package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector

public val Images.Icons.IcRecorder: ImageVector
    get() {
        if (_icRecorder != null) {
            return _icRecorder!!
        }
        _icRecorder = imageVector(
        	name = "IcRecorder",
        	width = 28f,
        	height = 28f,
        	viewportWidth = 32.0f,
        	viewportHeight = 32.0f,
        	autoMirror = false
        ) {
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF000000)),
            	strokeLineJoin = Round,
            	strokeLineWidth = 2.0f
            ) {
                moveTo(16.0f, 1.0f)
                lineTo(16.0f, 1.0f)
                curveToRelative(-2.761f, 0.0f, -5.0f, 2.239f, -5.0f, 5.0f)
                verticalLineToRelative(8.0f)
                curveToRelative(0.0f, 2.761f, 2.239f, 5.0f, 5.0f, 5.0f)
                horizontalLineToRelative(0.0f)
                curveToRelative(2.761f, 0.0f, 5.0f, -2.239f, 5.0f, -5.0f)
                verticalLineTo(6.0f)
                curveTo(21.0f, 3.239f, 18.761f, 1.0f, 16.0f, 1.0f)
                close()
            }
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF000000)),
            	strokeLineJoin = Round,
            	strokeLineWidth = 2.0f
            ) {
                moveTo(7.0f, 12.0f)
                verticalLineToRelative(2.0f)
                curveToRelative(0.0f, 4.971f, 4.029f, 9.0f, 9.0f, 9.0f)
                horizontalLineToRelative(0.0f)
                curveToRelative(4.971f, 0.0f, 9.0f, -4.029f, 9.0f, -9.0f)
                verticalLineToRelative(-2.0f)
            }
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF000000)),
            	strokeLineJoin = Round,
            	strokeLineWidth = 2.0f
            ) {
                moveTo(16.0f, 23.0f)
                lineTo(16.0f, 32.0f)
            }
        }
        return _icRecorder!!
    }

private var _icRecorder: ImageVector? = null
