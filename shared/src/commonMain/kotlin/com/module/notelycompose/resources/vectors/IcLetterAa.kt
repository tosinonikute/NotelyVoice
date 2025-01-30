package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector

public val Images.Icons.IcLetterAa: ImageVector
    get() {
        if (_icLetterAa != null) {
            return _icLetterAa!!
        }
        _icLetterAa = imageVector(
        	name = "IcLetterAa",
        	width = 32f,
        	height = 32f,
        	viewportWidth = 256.0f,
        	viewportHeight = 256.0f,
        	autoMirror = false
        ) {
            path(
            	fill = SolidColor(Color(0xFF000000))
            ) {
                moveTo(208.0f, 94.9f)
                arcToRelative(42.22f, 42.22f, 0.0f, false, false, -29.64f, 11.8f)
                arcToRelative(8.0f, 8.0f, 0.0f, true, false, 11.27f, 11.36f)
                arcTo(26.12f, 26.12f, 0.0f, false, true, 208.0f, 110.9f)
                curveToRelative(13.23f, 0.0f, 24.0f, 8.97f, 24.0f, 20.0f)
                verticalLineToRelative(7.22f)
                arcToRelative(42.76f, 42.76f, 0.0f, false, false, -24.0f, -7.22f)
                curveToRelative(-22.06f, 0.0f, -40.0f, 16.15f, -40.0f, 36.0f)
                reflectiveCurveToRelative(17.94f, 36.0f, 40.0f, 36.0f)
                arcToRelative(42.66f, 42.66f, 0.0f, false, false, 24.67f, -7.7f)
                arcTo(8.0f, 8.0f, 0.0f, false, false, 248.0f, 192.0f)
                lineTo(248.0f, 130.9f)
                curveTo(248.0f, 111.05f, 230.06f, 94.9f, 208.0f, 94.9f)
                close()
                moveTo(208.0f, 186.9f)
                curveToRelative(-13.23f, 0.0f, -24.0f, -8.97f, -24.0f, -20.0f)
                reflectiveCurveToRelative(10.77f, -20.0f, 24.0f, -20.0f)
                reflectiveCurveToRelative(24.0f, 8.97f, 24.0f, 20.0f)
                reflectiveCurveTo(221.23f, 186.9f, 208.0f, 186.9f)
                close()
                moveTo(137.87f, 148.2f)
                lineTo(137.85f, 148.18f)
                lineTo(87.07f, 52.26f)
                arcToRelative(8.0f, 8.0f, 0.0f, false, false, -14.14f, 0.0f)
                lineTo(22.15f, 148.18f)
                lineToRelative(-0.01f, 0.03f)
                lineTo(0.93f, 188.26f)
                arcToRelative(8.0f, 8.0f, 0.0f, false, false, 14.14f, 7.49f)
                lineTo(33.99f, 160.0f)
                horizontalLineToRelative(92.01f)
                lineToRelative(18.92f, 35.74f)
                arcToRelative(8.0f, 8.0f, 0.0f, false, false, 14.14f, -7.49f)
                close()
                moveTo(42.46f, 144.0f)
                lineTo(80.0f, 73.1f)
                lineTo(117.54f, 144.0f)
                close()
            }
        }
        return _icLetterAa!!
    }

private var _icLetterAa: ImageVector? = null
