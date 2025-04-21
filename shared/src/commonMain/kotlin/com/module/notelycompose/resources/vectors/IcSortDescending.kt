package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector

public val Images.Icons.IcSortDescending: ImageVector
    get() {
        if (_icSortDescending != null) {
            return _icSortDescending!!
        }
        _icSortDescending = imageVector(
        	name = "IcSortDescending",
        	width = 36f,
        	height = 36f,
        	viewportWidth = 20.0f,
        	viewportHeight = 20.0f,
        	autoMirror = false
        ) {
            path(
            	fill = SolidColor(Color(0xFF000000))
            ) {
                moveToRelative(3.0f, 3.0f)
                curveToRelative(-0.552f, 0.0f, -1.0f, 0.448f, -1.0f, 1.0f)
                reflectiveCurveToRelative(0.448f, 1.0f, 1.0f, 1.0f)
                horizontalLineToRelative(11.0f)
                curveToRelative(0.552f, 0.0f, 1.0f, -0.448f, 1.0f, -1.0f)
                reflectiveCurveToRelative(-0.448f, -1.0f, -1.0f, -1.0f)
                close()
            }
            path(
            	fill = SolidColor(Color(0xFF000000))
            ) {
                moveToRelative(3.0f, 7.0f)
                curveToRelative(-0.552f, 0.0f, -1.0f, 0.448f, -1.0f, 1.0f)
                reflectiveCurveToRelative(0.448f, 1.0f, 1.0f, 1.0f)
                horizontalLineToRelative(7.0f)
                curveToRelative(0.552f, 0.0f, 1.0f, -0.448f, 1.0f, -1.0f)
                reflectiveCurveToRelative(-0.448f, -1.0f, -1.0f, -1.0f)
                close()
            }
            path(
            	fill = SolidColor(Color(0xFF000000))
            ) {
                moveToRelative(3.0f, 11.0f)
                curveToRelative(-0.552f, 0.0f, -1.0f, 0.448f, -1.0f, 1.0f)
                reflectiveCurveToRelative(0.448f, 1.0f, 1.0f, 1.0f)
                horizontalLineToRelative(4.0f)
                curveToRelative(0.552f, 0.0f, 1.0f, -0.448f, 1.0f, -1.0f)
                reflectiveCurveToRelative(-0.448f, -1.0f, -1.0f, -1.0f)
                close()
            }
            path(
            	fill = SolidColor(Color(0xFF000000))
            ) {
                moveToRelative(15.0f, 8.0f)
                curveToRelative(0.0f, -0.552f, -0.448f, -1.0f, -1.0f, -1.0f)
                reflectiveCurveToRelative(-1.0f, 0.448f, -1.0f, 1.0f)
                verticalLineToRelative(5.586f)
                lineToRelative(-1.293f, -1.293f)
                curveToRelative(-0.391f, -0.391f, -1.024f, -0.391f, -1.414f, 0.0f)
                curveToRelative(-0.391f, 0.391f, -0.391f, 1.024f, 0.0f, 1.414f)
                lineToRelative(3.0f, 3.0f)
                curveToRelative(0.188f, 0.188f, 0.442f, 0.293f, 0.707f, 0.293f)
                reflectiveCurveToRelative(0.52f, -0.105f, 0.707f, -0.293f)
                lineToRelative(3.0f, -3.0f)
                curveToRelative(0.391f, -0.391f, 0.391f, -1.024f, 0.0f, -1.414f)
                reflectiveCurveToRelative(-1.024f, -0.391f, -1.414f, 0.0f)
                lineToRelative(-1.293f, 1.293f)
                close()
            }
        }
        return _icSortDescending!!
    }

private var _icSortDescending: ImageVector? = null
