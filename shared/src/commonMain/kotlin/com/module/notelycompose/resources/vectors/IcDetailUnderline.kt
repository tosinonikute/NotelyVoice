package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector

public val Images.Icons.IcDetailUnderline: ImageVector
    get() {
        if (_icDetailUnderline != null) {
            return _icDetailUnderline!!
        }
        _icDetailUnderline = imageVector(
        	name = "IcDetailUnderline",
        	width = 24f,
        	height = 24f,
        	viewportWidth = 20.0f,
        	viewportHeight = 20.0f,
        	autoMirror = false
        ) {
            path(
            	fill = SolidColor(Color(0xFF000000)),
            	pathFillType = EvenOdd,
            	stroke = SolidColor(Color(0x00000000)),
            	strokeLineWidth = 1.0f
            ) {
                moveTo(0.0f, 20.0f)
                lineTo(20.0f, 20.0f)
                lineTo(20.0f, 18.0f)
                lineTo(0.0f, 18.0f)
                lineTo(0.0f, 20.0f)
                close()
                moveTo(2.0f, 7.0f)
                lineTo(2.0f, 0.0f)
                lineTo(4.0f, 0.0f)
                lineTo(4.0f, 7.0f)
                curveTo(4.0f, 16.333f, 16.0f, 16.333f, 16.0f, 7.0f)
                lineTo(16.0f, 0.0f)
                lineTo(18.0f, 0.0f)
                lineTo(18.0f, 7.0f)
                curveTo(18.0f, 19.0f, 2.0f, 19.0f, 2.0f, 7.0f)
                lineTo(2.0f, 7.0f)
                close()
            }
        }
        return _icDetailUnderline!!
    }

private var _icDetailUnderline: ImageVector? = null
