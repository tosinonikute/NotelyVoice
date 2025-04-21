package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector

public val Images.Icons.IcHome: ImageVector
    get() {
        if (_icHome != null) {
            return _icHome!!
        }
        _icHome = imageVector(
        	name = "IcHome",
        	width = 16f,
        	height = 16f,
        	viewportWidth = 16.0f,
        	viewportHeight = 16.0f,
        	autoMirror = false
        ) {
            path(
            	fill = SolidColor(Color(0xFF000000))
            ) {
                moveTo(1.0f, 6.0f)
                verticalLineTo(15.0f)
                horizontalLineTo(6.0f)
                verticalLineTo(11.0f)
                curveTo(6.0f, 9.895f, 6.895f, 9.0f, 8.0f, 9.0f)
                curveTo(9.105f, 9.0f, 10.0f, 9.895f, 10.0f, 11.0f)
                verticalLineTo(15.0f)
                horizontalLineTo(15.0f)
                verticalLineTo(6.0f)
                lineTo(8.0f, 0.0f)
                lineTo(1.0f, 6.0f)
                close()
            }
        }
        return _icHome!!
    }

private var _icHome: ImageVector? = null
