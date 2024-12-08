package com.module.notelycompose.resources.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import dev.sergiobelda.compose.vectorize.core.imageVector

public val Images.Icons.IcSettings: ImageVector
    get() {
        if (_icSettings != null) {
            return _icSettings!!
        }
        _icSettings = imageVector(
        	name = "IcSettings",
        	width = 24f,
        	height = 24f,
        	viewportWidth = 24.0f,
        	viewportHeight = 24.0f,
        	autoMirror = false
        ) {
            path(
            	fill = SolidColor(Color(0xFF1C274C)),
            	pathFillType = EvenOdd
            ) {
                moveTo(12.0f, 8.25f)
                curveTo(9.929f, 8.25f, 8.25f, 9.929f, 8.25f, 12.0f)
                curveTo(8.25f, 14.071f, 9.929f, 15.75f, 12.0f, 15.75f)
                curveTo(14.071f, 15.75f, 15.75f, 14.071f, 15.75f, 12.0f)
                curveTo(15.75f, 9.929f, 14.071f, 8.25f, 12.0f, 8.25f)
                close()
                moveTo(9.75f, 12.0f)
                curveTo(9.75f, 10.757f, 10.757f, 9.75f, 12.0f, 9.75f)
                curveTo(13.243f, 9.75f, 14.25f, 10.757f, 14.25f, 12.0f)
                curveTo(14.25f, 13.243f, 13.243f, 14.25f, 12.0f, 14.25f)
                curveTo(10.757f, 14.25f, 9.75f, 13.243f, 9.75f, 12.0f)
                close()
            }
            path(
            	fill = SolidColor(Color(0xFF1C274C)),
            	pathFillType = EvenOdd
            ) {
                moveTo(11.975f, 1.25f)
                curveTo(11.53f, 1.25f, 11.159f, 1.25f, 10.855f, 1.271f)
                curveTo(10.538f, 1.292f, 10.238f, 1.339f, 9.948f, 1.459f)
                curveTo(9.274f, 1.738f, 8.738f, 2.274f, 8.459f, 2.948f)
                curveTo(8.314f, 3.298f, 8.275f, 3.668f, 8.26f, 4.07f)
                curveTo(8.248f, 4.393f, 8.085f, 4.663f, 7.844f, 4.801f)
                curveTo(7.603f, 4.94f, 7.288f, 4.947f, 7.003f, 4.796f)
                curveTo(6.647f, 4.608f, 6.307f, 4.457f, 5.931f, 4.407f)
                curveTo(5.208f, 4.312f, 4.476f, 4.508f, 3.898f, 4.952f)
                curveTo(3.648f, 5.144f, 3.458f, 5.38f, 3.281f, 5.643f)
                curveTo(3.111f, 5.897f, 2.925f, 6.218f, 2.703f, 6.603f)
                lineTo(2.678f, 6.647f)
                curveTo(2.455f, 7.032f, 2.27f, 7.353f, 2.136f, 7.627f)
                curveTo(1.996f, 7.913f, 1.886f, 8.195f, 1.845f, 8.507f)
                curveTo(1.75f, 9.23f, 1.946f, 9.961f, 2.39f, 10.54f)
                curveTo(2.621f, 10.841f, 2.922f, 11.06f, 3.262f, 11.274f)
                curveTo(3.536f, 11.446f, 3.688f, 11.722f, 3.688f, 12.0f)
                curveTo(3.688f, 12.278f, 3.536f, 12.554f, 3.262f, 12.726f)
                curveTo(2.922f, 12.94f, 2.621f, 13.159f, 2.39f, 13.46f)
                curveTo(1.946f, 14.038f, 1.75f, 14.77f, 1.845f, 15.493f)
                curveTo(1.886f, 15.804f, 1.996f, 16.087f, 2.136f, 16.373f)
                curveTo(2.27f, 16.647f, 2.455f, 16.968f, 2.678f, 17.353f)
                lineTo(2.703f, 17.397f)
                curveTo(2.925f, 17.782f, 3.111f, 18.103f, 3.281f, 18.357f)
                curveTo(3.458f, 18.62f, 3.648f, 18.856f, 3.898f, 19.048f)
                curveTo(4.476f, 19.492f, 5.208f, 19.688f, 5.931f, 19.593f)
                curveTo(6.307f, 19.543f, 6.647f, 19.392f, 7.003f, 19.204f)
                curveTo(7.288f, 19.053f, 7.603f, 19.06f, 7.844f, 19.199f)
                curveTo(8.085f, 19.337f, 8.248f, 19.607f, 8.26f, 19.93f)
                curveTo(8.275f, 20.332f, 8.314f, 20.702f, 8.459f, 21.052f)
                curveTo(8.738f, 21.726f, 9.274f, 22.262f, 9.948f, 22.541f)
                curveTo(10.238f, 22.661f, 10.538f, 22.708f, 10.855f, 22.729f)
                curveTo(11.159f, 22.75f, 11.53f, 22.75f, 11.975f, 22.75f)
                horizontalLineTo(12.025f)
                curveTo(12.47f, 22.75f, 12.841f, 22.75f, 13.145f, 22.729f)
                curveTo(13.462f, 22.708f, 13.762f, 22.661f, 14.052f, 22.541f)
                curveTo(14.726f, 22.262f, 15.262f, 21.726f, 15.541f, 21.052f)
                curveTo(15.686f, 20.702f, 15.725f, 20.332f, 15.74f, 19.93f)
                curveTo(15.752f, 19.607f, 15.915f, 19.337f, 16.156f, 19.198f)
                curveTo(16.397f, 19.06f, 16.712f, 19.053f, 16.997f, 19.204f)
                curveTo(17.353f, 19.392f, 17.693f, 19.543f, 18.069f, 19.592f)
                curveTo(18.792f, 19.688f, 19.524f, 19.492f, 20.102f, 19.048f)
                curveTo(20.352f, 18.856f, 20.542f, 18.62f, 20.719f, 18.357f)
                curveTo(20.889f, 18.103f, 21.075f, 17.782f, 21.297f, 17.397f)
                lineTo(21.322f, 17.353f)
                curveTo(21.545f, 16.968f, 21.73f, 16.647f, 21.864f, 16.373f)
                curveTo(22.004f, 16.087f, 22.114f, 15.804f, 22.155f, 15.493f)
                curveTo(22.25f, 14.77f, 22.054f, 14.038f, 21.61f, 13.46f)
                curveTo(21.379f, 13.159f, 21.078f, 12.94f, 20.738f, 12.726f)
                curveTo(20.464f, 12.554f, 20.312f, 12.278f, 20.312f, 12.0f)
                curveTo(20.312f, 11.722f, 20.464f, 11.446f, 20.738f, 11.274f)
                curveTo(21.078f, 11.06f, 21.379f, 10.841f, 21.61f, 10.54f)
                curveTo(22.054f, 9.961f, 22.25f, 9.23f, 22.155f, 8.507f)
                curveTo(22.114f, 8.195f, 22.004f, 7.913f, 21.865f, 7.627f)
                curveTo(21.73f, 7.353f, 21.545f, 7.032f, 21.322f, 6.647f)
                lineTo(21.297f, 6.603f)
                curveTo(21.075f, 6.218f, 20.889f, 5.897f, 20.719f, 5.643f)
                curveTo(20.542f, 5.38f, 20.352f, 5.144f, 20.102f, 4.952f)
                curveTo(19.524f, 4.508f, 18.792f, 4.312f, 18.069f, 4.407f)
                curveTo(17.693f, 4.457f, 17.353f, 4.608f, 16.997f, 4.796f)
                curveTo(16.712f, 4.947f, 16.397f, 4.94f, 16.156f, 4.801f)
                curveTo(15.915f, 4.663f, 15.752f, 4.393f, 15.74f, 4.07f)
                curveTo(15.725f, 3.668f, 15.686f, 3.298f, 15.541f, 2.948f)
                curveTo(15.262f, 2.274f, 14.726f, 1.738f, 14.052f, 1.459f)
                curveTo(13.762f, 1.339f, 13.462f, 1.292f, 13.145f, 1.271f)
                curveTo(12.841f, 1.25f, 12.47f, 1.25f, 12.025f, 1.25f)
                horizontalLineTo(11.975f)
                close()
                moveTo(10.522f, 2.845f)
                curveTo(10.599f, 2.813f, 10.716f, 2.784f, 10.957f, 2.767f)
                curveTo(11.204f, 2.75f, 11.524f, 2.75f, 12.0f, 2.75f)
                curveTo(12.476f, 2.75f, 12.796f, 2.75f, 13.043f, 2.767f)
                curveTo(13.284f, 2.784f, 13.401f, 2.813f, 13.478f, 2.845f)
                curveTo(13.785f, 2.972f, 14.028f, 3.215f, 14.155f, 3.522f)
                curveTo(14.195f, 3.618f, 14.228f, 3.769f, 14.241f, 4.126f)
                curveTo(14.271f, 4.918f, 14.68f, 5.681f, 15.406f, 6.1f)
                curveTo(16.132f, 6.52f, 16.997f, 6.492f, 17.698f, 6.122f)
                curveTo(18.014f, 5.955f, 18.161f, 5.908f, 18.265f, 5.895f)
                curveTo(18.594f, 5.851f, 18.926f, 5.94f, 19.189f, 6.142f)
                curveTo(19.255f, 6.193f, 19.34f, 6.28f, 19.474f, 6.48f)
                curveTo(19.612f, 6.686f, 19.773f, 6.963f, 20.011f, 7.375f)
                curveTo(20.249f, 7.787f, 20.408f, 8.064f, 20.517f, 8.287f)
                curveTo(20.624f, 8.504f, 20.657f, 8.62f, 20.667f, 8.703f)
                curveTo(20.711f, 9.032f, 20.622f, 9.364f, 20.42f, 9.627f)
                curveTo(20.356f, 9.71f, 20.242f, 9.814f, 19.94f, 10.004f)
                curveTo(19.268f, 10.426f, 18.812f, 11.162f, 18.812f, 12.0f)
                curveTo(18.812f, 12.838f, 19.268f, 13.574f, 19.94f, 13.996f)
                curveTo(20.242f, 14.186f, 20.356f, 14.29f, 20.42f, 14.373f)
                curveTo(20.622f, 14.636f, 20.711f, 14.968f, 20.667f, 15.297f)
                curveTo(20.656f, 15.38f, 20.623f, 15.496f, 20.517f, 15.713f)
                curveTo(20.408f, 15.936f, 20.249f, 16.212f, 20.011f, 16.625f)
                curveTo(19.772f, 17.037f, 19.612f, 17.314f, 19.474f, 17.52f)
                curveTo(19.339f, 17.72f, 19.255f, 17.807f, 19.189f, 17.858f)
                curveTo(18.926f, 18.059f, 18.594f, 18.149f, 18.265f, 18.105f)
                curveTo(18.161f, 18.092f, 18.014f, 18.045f, 17.698f, 17.878f)
                curveTo(16.997f, 17.507f, 16.132f, 17.48f, 15.406f, 17.899f)
                curveTo(14.68f, 18.319f, 14.271f, 19.082f, 14.241f, 19.874f)
                curveTo(14.228f, 20.231f, 14.195f, 20.382f, 14.155f, 20.478f)
                curveTo(14.028f, 20.785f, 13.785f, 21.028f, 13.478f, 21.155f)
                curveTo(13.401f, 21.187f, 13.284f, 21.216f, 13.043f, 21.233f)
                curveTo(12.796f, 21.25f, 12.476f, 21.25f, 12.0f, 21.25f)
                curveTo(11.524f, 21.25f, 11.204f, 21.25f, 10.957f, 21.233f)
                curveTo(10.716f, 21.216f, 10.599f, 21.187f, 10.522f, 21.155f)
                curveTo(10.215f, 21.028f, 9.972f, 20.785f, 9.845f, 20.478f)
                curveTo(9.805f, 20.382f, 9.772f, 20.231f, 9.759f, 19.874f)
                curveTo(9.729f, 19.082f, 9.32f, 18.319f, 8.594f, 17.899f)
                curveTo(7.868f, 17.48f, 7.003f, 17.508f, 6.302f, 17.878f)
                curveTo(5.986f, 18.045f, 5.839f, 18.092f, 5.735f, 18.105f)
                curveTo(5.406f, 18.149f, 5.074f, 18.059f, 4.811f, 17.858f)
                curveTo(4.745f, 17.807f, 4.66f, 17.72f, 4.526f, 17.52f)
                curveTo(4.388f, 17.314f, 4.227f, 17.037f, 3.989f, 16.625f)
                curveTo(3.751f, 16.213f, 3.592f, 15.936f, 3.483f, 15.713f)
                curveTo(3.376f, 15.496f, 3.343f, 15.38f, 3.332f, 15.297f)
                curveTo(3.289f, 14.968f, 3.378f, 14.636f, 3.58f, 14.373f)
                curveTo(3.644f, 14.29f, 3.758f, 14.186f, 4.06f, 13.996f)
                curveTo(4.732f, 13.574f, 5.188f, 12.838f, 5.188f, 12.0f)
                curveTo(5.188f, 11.162f, 4.732f, 10.426f, 4.06f, 10.004f)
                curveTo(3.758f, 9.814f, 3.644f, 9.71f, 3.58f, 9.627f)
                curveTo(3.378f, 9.364f, 3.289f, 9.031f, 3.333f, 8.703f)
                curveTo(3.343f, 8.62f, 3.377f, 8.504f, 3.483f, 8.287f)
                curveTo(3.592f, 8.064f, 3.751f, 7.787f, 3.989f, 7.375f)
                curveTo(4.227f, 6.963f, 4.388f, 6.686f, 4.526f, 6.48f)
                curveTo(4.661f, 6.28f, 4.745f, 6.193f, 4.811f, 6.142f)
                curveTo(5.074f, 5.94f, 5.406f, 5.851f, 5.735f, 5.895f)
                curveTo(5.839f, 5.908f, 5.986f, 5.955f, 6.302f, 6.122f)
                curveTo(7.003f, 6.492f, 7.868f, 6.52f, 8.594f, 6.1f)
                curveTo(9.32f, 5.681f, 9.729f, 4.918f, 9.759f, 4.126f)
                curveTo(9.772f, 3.769f, 9.805f, 3.618f, 9.845f, 3.522f)
                curveTo(9.972f, 3.215f, 10.215f, 2.972f, 10.522f, 2.845f)
                close()
            }
        }
        return _icSettings!!
    }

private var _icSettings: ImageVector? = null
