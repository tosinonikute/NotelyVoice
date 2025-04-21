package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector

public val Images.Icons.IcStarFilled: ImageVector
    get() {
        if (_icStarFilled != null) {
            return _icStarFilled!!
        }
        _icStarFilled = imageVector(
            name = "IcStarFilled",
            width = 24f,
            height = 24f,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f,
            autoMirror = false
        ) {
            path(
                fill = SolidColor(Color(0xFF1C274C))
            ) {
                moveTo(9.153f, 5.408f)
                curveTo(10.42f, 3.136f, 11.053f, 2.0f, 12.0f, 2.0f)
                curveTo(12.947f, 2.0f, 13.58f, 3.136f, 14.847f, 5.408f)
                lineTo(15.175f, 5.996f)
                curveTo(15.535f, 6.642f, 15.714f, 6.965f, 15.995f, 7.178f)
                curveTo(16.276f, 7.391f, 16.625f, 7.47f, 17.324f, 7.628f)
                lineTo(17.961f, 7.772f)
                curveTo(20.42f, 8.329f, 21.65f, 8.607f, 21.943f, 9.548f)
                curveTo(22.235f, 10.489f, 21.397f, 11.469f, 19.72f, 13.43f)
                lineTo(19.286f, 13.937f)
                curveTo(18.81f, 14.494f, 18.571f, 14.773f, 18.464f, 15.118f)
                curveTo(18.357f, 15.462f, 18.393f, 15.834f, 18.465f, 16.578f)
                lineTo(18.531f, 17.254f)
                curveTo(18.784f, 19.871f, 18.911f, 21.179f, 18.145f, 21.76f)
                curveTo(17.379f, 22.342f, 16.227f, 21.812f, 13.924f, 20.751f)
                lineTo(13.328f, 20.477f)
                curveTo(12.674f, 20.176f, 12.347f, 20.025f, 12.0f, 20.025f)
                curveTo(11.653f, 20.025f, 11.326f, 20.176f, 10.672f, 20.477f)
                lineTo(10.076f, 20.751f)
                curveTo(7.773f, 21.812f, 6.621f, 22.342f, 5.855f, 21.76f)
                curveTo(5.089f, 21.179f, 5.216f, 19.871f, 5.469f, 17.254f)
                lineTo(5.535f, 16.578f)
                curveTo(5.607f, 15.834f, 5.643f, 15.462f, 5.536f, 15.118f)
                curveTo(5.429f, 14.773f, 5.19f, 14.494f, 4.714f, 13.937f)
                lineTo(4.28f, 13.43f)
                curveTo(2.603f, 11.469f, 1.765f, 10.489f, 2.057f, 9.548f)
                curveTo(2.35f, 8.607f, 3.58f, 8.329f, 6.04f, 7.772f)
                lineTo(6.676f, 7.628f)
                curveTo(7.375f, 7.47f, 7.724f, 7.391f, 8.005f, 7.178f)
                curveTo(8.286f, 6.965f, 8.466f, 6.642f, 8.825f, 5.996f)
                lineTo(9.153f, 5.408f)
                close()
            }
        }
        return _icStarFilled!!
    }

private var _icStarFilled: ImageVector? = null
