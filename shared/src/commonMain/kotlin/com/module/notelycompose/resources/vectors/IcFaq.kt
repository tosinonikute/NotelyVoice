package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector

public val Images.Icons.IcFaq: ImageVector
    get() {
        if (_icFaq != null) {
            return _icFaq!!
        }
        _icFaq = imageVector(
        	name = "IcFaq",
        	width = 20f,
        	height = 20f,
        	viewportWidth = 512.0f,
        	viewportHeight = 512.0f,
        	autoMirror = false
        ) {
            path(
            	fill = SolidColor(Color(0xFF000000))
            ) {
                moveTo(256.0f, 0.0f)
                curveTo(114.61f, 0.0f, 0.0f, 114.62f, 0.0f, 256.0f)
                curveToRelative(0.0f, 141.39f, 114.61f, 256.0f, 256.0f, 256.0f)
                reflectiveCurveToRelative(256.0f, -114.61f, 256.0f, -256.0f)
                curveTo(512.0f, 114.62f, 397.39f, 0.0f, 256.0f, 0.0f)
                close()
                moveTo(256.0f, 384.0f)
                curveToRelative(-17.67f, 0.0f, -32.0f, -14.32f, -32.0f, -32.0f)
                curveToRelative(0.0f, -17.67f, 14.33f, -32.0f, 32.0f, -32.0f)
                reflectiveCurveToRelative(32.0f, 14.33f, 32.0f, 32.0f)
                curveTo(288.0f, 369.68f, 273.67f, 384.0f, 256.0f, 384.0f)
                close()
                moveTo(280.44f, 251.17f)
                curveToRelative(-5.05f, 2.08f, -8.44f, 7.75f, -8.44f, 14.11f)
                verticalLineTo(272.0f)
                curveToRelative(0.0f, 8.84f, -7.16f, 16.0f, -16.0f, 16.0f)
                curveToRelative(-8.84f, 0.0f, -16.0f, -7.16f, -16.0f, -16.0f)
                verticalLineToRelative(-6.72f)
                curveToRelative(0.0f, -19.45f, 11.08f, -36.61f, 28.22f, -43.69f)
                curveToRelative(14.0f, -5.8f, 21.92f, -20.39f, 19.25f, -35.52f)
                curveToRelative(-2.2f, -12.59f, -12.95f, -23.34f, -25.53f, -25.55f)
                curveToRelative(-9.75f, -1.72f, -19.14f, 0.77f, -26.5f, 6.95f)
                curveTo(228.17f, 173.59f, 224.0f, 182.53f, 224.0f, 192.0f)
                curveToRelative(0.0f, 8.84f, -7.16f, 16.0f, -16.0f, 16.0f)
                curveToRelative(-8.84f, 0.0f, -16.0f, -7.16f, -16.0f, -16.0f)
                curveToRelative(0.0f, -18.95f, 8.33f, -36.83f, 22.86f, -49.03f)
                curveToRelative(14.52f, -12.19f, 33.66f, -17.27f, 52.61f, -13.95f)
                curveToRelative(25.81f, 4.53f, 47.0f, 25.72f, 51.53f, 51.53f)
                curveTo(324.27f, 210.55f, 308.42f, 239.59f, 280.44f, 251.17f)
                close()
            }
        }
        return _icFaq!!
    }

private var _icFaq: ImageVector? = null
