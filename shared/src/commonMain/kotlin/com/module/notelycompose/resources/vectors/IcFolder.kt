package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector
import androidx.compose.ui.graphics.StrokeCap.Companion.Round as strokeCapRound
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round as strokeJoinRound

public val Images.Icons.IcFolder: ImageVector
    get() {
        if (_icFolder != null) {
            return _icFolder!!
        }
        _icFolder = imageVector(
        	name = "IcFolder",
        	width = 24f,
        	height = 24f,
        	viewportWidth = 24.0f,
        	viewportHeight = 24.0f,
        	autoMirror = false
        ) {
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF292D32)),
            	strokeLineWidth = 1.5f
            ) {
                moveToRelative(21.67f, 14.3f)
                lineToRelative(-0.4f, 5.0f)
                curveToRelative(-0.15f, 1.53f, -0.27f, 2.7f, -2.98f, 2.7f)
                horizontalLineToRelative(-12.58f)
                curveToRelative(-2.71f, 0.0f, -2.83f, -1.17f, -2.98f, -2.7f)
                lineToRelative(-0.4f, -5.0f)
                curveToRelative(-0.08f, -0.83f, 0.18f, -1.6f, 0.65f, -2.19f)
                curveToRelative(0.01f, -0.01f, 0.01f, -0.01f, 0.02f, -0.02f)
                curveToRelative(0.55f, -0.67f, 1.38f, -1.09f, 2.31f, -1.09f)
                horizontalLineToRelative(13.38f)
                curveToRelative(0.93f, 0.0f, 1.75f, 0.42f, 2.29f, 1.07f)
                curveToRelative(0.01f, 0.01f, 0.02f, 0.02f, 0.02f, 0.03f)
                curveToRelative(0.49f, 0.59f, 0.76f, 1.36f, 0.67f, 2.2f)
                close()
            }
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF292D32)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 1.5f
            ) {
                moveToRelative(3.5f, 11.43f)
                verticalLineToRelative(-5.15f)
                curveToRelative(0.0f, -3.4f, 0.85f, -4.25f, 4.25f, -4.25f)
                horizontalLineToRelative(1.27f)
                curveToRelative(1.27f, 0.0f, 1.56f, 0.38f, 2.04f, 1.02f)
                lineToRelative(1.27f, 1.7f)
                curveToRelative(0.32f, 0.42f, 0.51f, 0.68f, 1.36f, 0.68f)
                horizontalLineToRelative(2.55f)
                curveToRelative(3.4f, 0.0f, 4.25f, 0.85f, 4.25f, 4.25f)
                verticalLineToRelative(1.79f)
            }
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF292D32)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 1.5f
            ) {
                moveToRelative(9.43f, 17.0f)
                horizontalLineToRelative(5.14f)
            }
        }
        return _icFolder!!
    }

private var _icFolder: ImageVector? = null
