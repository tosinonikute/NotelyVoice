package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector

public val Images.Icons.IcDetailBold: ImageVector
    get() {
        if (_icDetailBold != null) {
            return _icDetailBold!!
        }
        _icDetailBold = imageVector(
        	name = "IcDetailBold",
        	width = 24f,
        	height = 24f,
        	viewportWidth = 20.0f,
        	viewportHeight = 20.0f,
        	autoMirror = false
        ) {
            path(
            	fill = SolidColor(Color(0xFF000000)),
            	pathFillType = EvenOdd
            ) {
                moveTo(4.0f, 1.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, false, -1.0f, 1.0f)
                verticalLineToRelative(16.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, false, false, 1.0f, 1.0f)
                verticalLineToRelative(-1.0f)
                verticalLineToRelative(1.0f)
                horizontalLineToRelative(8.0f)
                arcToRelative(5.0f, 5.0f, 0.0f, false, false, 1.745f, -9.687f)
                arcTo(5.0f, 5.0f, 0.0f, false, false, 10.0f, 1.0f)
                lineTo(4.0f, 1.0f)
                close()
                moveTo(10.0f, 9.0f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, false, 0.0f, -6.0f)
                lineTo(5.0f, 3.0f)
                verticalLineToRelative(6.0f)
                horizontalLineToRelative(5.0f)
                close()
                moveTo(5.0f, 11.0f)
                verticalLineToRelative(6.0f)
                horizontalLineToRelative(7.0f)
                arcToRelative(3.0f, 3.0f, 0.0f, true, false, 0.0f, -6.0f)
                lineTo(5.0f, 11.0f)
                close()
            }
        }
        return _icDetailBold!!
    }

private var _icDetailBold: ImageVector? = null
