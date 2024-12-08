package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector
import androidx.compose.ui.graphics.StrokeCap.Companion.Round as strokeCapRound
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round as strokeJoinRound

public val Images.Icons.IcDetailRedo: ImageVector
    get() {
        if (_icDetailRedo != null) {
            return _icDetailRedo!!
        }
        _icDetailRedo = imageVector(
        	name = "IcDetailRedo",
        	width = 32f,
        	height = 32f,
        	viewportWidth = 24.0f,
        	viewportHeight = 24.0f,
        	autoMirror = false
        ) {
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF000000)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 1.5f
            ) {
                moveTo(20.0f, 9.0f)
                verticalLineTo(15.0f)
                moveTo(20.0f, 15.0f)
                horizontalLineTo(14.0f)
                moveTo(20.0f, 15.0f)
                curveTo(17.673f, 12.911f, 15.517f, 10.547f, 12.255f, 10.088f)
                curveTo(10.322f, 9.816f, 8.354f, 10.179f, 6.646f, 11.123f)
                curveTo(4.938f, 12.068f, 3.584f, 13.541f, 2.786f, 15.322f)
            }
        }
        return _icDetailRedo!!
    }

private var _icDetailRedo: ImageVector? = null
