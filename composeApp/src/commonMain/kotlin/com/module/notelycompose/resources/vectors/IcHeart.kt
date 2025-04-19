package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector
import androidx.compose.ui.graphics.StrokeCap.Companion.Round as strokeCapRound
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round as strokeJoinRound

public val Images.Icons.IcHeart: ImageVector
    get() {
        if (_icHeart != null) {
            return _icHeart!!
        }
        _icHeart = imageVector(
        	name = "IcHeart",
        	width = 24f,
        	height = 24f,
        	viewportWidth = 24.0f,
        	viewportHeight = 24.0f,
        	autoMirror = false
        ) {
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF292D32)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 1.5f
            ) {
                moveToRelative(12.62f, 20.81f)
                curveToRelative(-0.34f, 0.12f, -0.9f, 0.12f, -1.24f, 0.0f)
                curveToRelative(-2.9f, -0.99f, -9.38f, -5.12f, -9.38f, -12.12f)
                curveToRelative(0.0f, -3.09f, 2.49f, -5.59f, 5.56f, -5.59f)
                curveToRelative(1.82f, 0.0f, 3.43f, 0.88f, 4.44f, 2.24f)
                curveToRelative(1.01f, -1.36f, 2.63f, -2.24f, 4.44f, -2.24f)
                curveToRelative(3.07f, 0.0f, 5.56f, 2.5f, 5.56f, 5.59f)
                curveToRelative(0.0f, 7.0f, -6.48f, 11.13f, -9.38f, 12.12f)
                close()
            }
        }
        return _icHeart!!
    }

private var _icHeart: ImageVector? = null
