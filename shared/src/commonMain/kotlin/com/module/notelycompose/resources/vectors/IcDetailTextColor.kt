package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector
import androidx.compose.ui.graphics.StrokeCap.Companion.Round as strokeCapRound
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round as strokeJoinRound

public val Images.Icons.IcDetailTextColor: ImageVector
    get() {
        if (_icDetailTextColor != null) {
            return _icDetailTextColor!!
        }
        _icDetailTextColor = imageVector(
        	name = "IcDetailTextColor",
        	width = 32f,
        	height = 32f,
        	viewportWidth = 32.0f,
        	viewportHeight = 32.0f,
        	autoMirror = false
        ) {
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF000000)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 2.0f
            ) {
                moveTo(3.0f, 28.0f)
                lineTo(29.0f, 28.0f)
            }
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF000000)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 2.0f
            ) {
                moveTo(5.0f, 23.0f)
                lineTo(9.0f, 23.0f)
            }
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF000000)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 2.0f
            ) {
                moveTo(23.0f, 23.0f)
                lineTo(27.0f, 23.0f)
            }
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF000000)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 2.0f
            ) {
                moveTo(7.0f, 23.0f)
                lineToRelative(9.0f, -19.0f)
                lineToRelative(9.0f, 19.0f)
            }
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF000000)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 2.0f
            ) {
                moveTo(10.0f, 17.0f)
                lineTo(22.0f, 17.0f)
            }
        }
        return _icDetailTextColor!!
    }

private var _icDetailTextColor: ImageVector? = null
