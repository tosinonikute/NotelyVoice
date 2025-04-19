package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector
import androidx.compose.ui.graphics.StrokeCap.Companion.Round as strokeCapRound
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round as strokeJoinRound

public val Images.Icons.SortDescending: ImageVector
    get() {
        if (_sortDescending != null) {
            return _sortDescending!!
        }
        _sortDescending = imageVector(
        	name = "SortDescending",
        	width = 28f,
        	height = 28f,
        	viewportWidth = 24.0f,
        	viewportHeight = 24.0f,
        	autoMirror = false
        ) {
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF4A5568)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 2.0f
            ) {
                moveToRelative(3.0f, 4.0f)
                horizontalLineToRelative(13.0f)
                moveToRelative(-13.0f, 4.0f)
                horizontalLineToRelative(9.0f)
                moveToRelative(-9.0f, 4.0f)
                horizontalLineToRelative(9.0f)
                moveToRelative(5.0f, -4.0f)
                verticalLineToRelative(12.0f)
                moveToRelative(0.0f, 0.0f)
                lineToRelative(-4.0f, -4.0f)
                moveToRelative(4.0f, 4.0f)
                lineToRelative(4.0f, -4.0f)
            }
        }
        return _sortDescending!!
    }

private var _sortDescending: ImageVector? = null
