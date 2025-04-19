package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector
import androidx.compose.ui.graphics.StrokeCap.Companion.Round as strokeCapRound
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round as strokeJoinRound

public val Images.Icons.IcDetailShare: ImageVector
    get() {
        if (_icDetailShare != null) {
            return _icDetailShare!!
        }
        _icDetailShare = imageVector(
        	name = "IcDetailShare",
        	width = 24f,
        	height = 24f,
        	viewportWidth = 24.0f,
        	viewportHeight = 24.0f,
        	autoMirror = false
        ) {
            path(
            	fill = SolidColor(Color(0x00000000)),
            	stroke = SolidColor(Color(0xFF000000)),
            	strokeLineCap = strokeCapRound,
            	strokeLineJoin = strokeJoinRound,
            	strokeLineWidth = 2.0f
            ) {
                moveTo(9.0f, 6.0f)
                lineTo(12.0f, 3.0f)
                moveTo(12.0f, 3.0f)
                lineTo(15.0f, 6.0f)
                moveTo(12.0f, 3.0f)
                verticalLineTo(13.0f)
                moveTo(7.0f, 10.0f)
                curveTo(6.068f, 10.0f, 5.602f, 10.0f, 5.235f, 10.152f)
                curveTo(4.745f, 10.355f, 4.355f, 10.745f, 4.152f, 11.235f)
                curveTo(4.0f, 11.602f, 4.0f, 12.068f, 4.0f, 13.0f)
                verticalLineTo(17.8f)
                curveTo(4.0f, 18.92f, 4.0f, 19.48f, 4.218f, 19.908f)
                curveTo(4.41f, 20.284f, 4.715f, 20.59f, 5.092f, 20.782f)
                curveTo(5.519f, 21.0f, 6.079f, 21.0f, 7.197f, 21.0f)
                horizontalLineTo(16.804f)
                curveTo(17.921f, 21.0f, 18.48f, 21.0f, 18.908f, 20.782f)
                curveTo(19.284f, 20.59f, 19.59f, 20.284f, 19.782f, 19.908f)
                curveTo(20.0f, 19.48f, 20.0f, 18.921f, 20.0f, 17.803f)
                verticalLineTo(13.0f)
                curveTo(20.0f, 12.068f, 20.0f, 11.602f, 19.848f, 11.235f)
                curveTo(19.645f, 10.745f, 19.255f, 10.355f, 18.765f, 10.152f)
                curveTo(18.398f, 10.0f, 17.932f, 10.0f, 17.0f, 10.0f)
            }
        }
        return _icDetailShare!!
    }

private var _icDetailShare: ImageVector? = null
