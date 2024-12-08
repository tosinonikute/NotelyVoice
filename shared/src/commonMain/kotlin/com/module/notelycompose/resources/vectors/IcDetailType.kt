package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector
import androidx.compose.ui.graphics.StrokeCap.Companion.Round as strokeCapRound
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round as strokeJoinRound

public val Images.Icons.IcDetailType: ImageVector
    get() {
        if (_icDetailType != null) {
            return _icDetailType!!
        }
        _icDetailType = imageVector(
        	name = "IcDetailType",
        	width = 24f,
        	height = 24f,
        	viewportWidth = 24.0f,
        	viewportHeight = 24.0f,
        	autoMirror = false
        ) {
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF333333)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 2.0f
            ) {
                moveTo(12.0f, 6.0f)
                lineTo(12.0f, 19.0f)
            }
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF333333)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 2.0f
            ) {
                moveTo(5.0f, 5.0f)
                lineTo(5.0f, 8.0f)
            }
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF333333)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 2.0f
            ) {
                moveTo(19.0f, 5.0f)
                lineTo(19.0f, 8.0f)
            }
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF333333)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 2.0f
            ) {
                moveTo(19.0f, 5.0f)
                lineTo(5.0f, 5.0f)
            }
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF333333)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 2.0f
            ) {
                moveTo(15.0f, 20.0f)
                lineTo(9.0f, 20.0f)
            }
        }
        return _icDetailType!!
    }

private var _icDetailType: ImageVector? = null
