package com.module.notelycompose.audio.ui.uicomponents

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults.colors
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSlider(modifier: Modifier = Modifier) {
    var value by remember { mutableFloatStateOf(0f) }
    val interactionSource = remember { MutableInteractionSource() }
    Slider(
        value = value,
        onValueChange = { value = it },
        interactionSource = interactionSource,
        thumb = { Thumb(interactionSource) },
        track = { Track(it) },
        modifier = modifier
    )
}

/**
 *
 * Copied from older material 3 library source code
 * Reference: http://sinasamaki.com/custom-material-3-sliders-in-jetpack-compose/
 */
@Composable
@ExperimentalMaterial3Api
fun Track(
    sliderState: SliderState,
    modifier: Modifier = Modifier,
    colors: SliderColors = colors(),
    enabled: Boolean = true
) {
    val inactiveTrackColor = colors.trackColor(enabled, active = false)
    val activeTrackColor = colors.trackColor(enabled, active = true)
    val inactiveTickColor = colors.tickColor(enabled, active = false)
    val activeTickColor = colors.tickColor(enabled, active = true)
    Canvas(
        modifier
            .fillMaxWidth()
            .height(TrackHeight)
    ) {
        drawTrack(
            sliderState.tickFractions,
            0f,
            sliderState.coercedValueAsFraction,
            inactiveTrackColor,
            activeTrackColor,
            inactiveTickColor,
            activeTickColor
        )
    }
}


private fun DrawScope.drawTrack(
    tickFractions: FloatArray,
    activeRangeStart: Float,
    activeRangeEnd: Float,
    inactiveTrackColor: Color,
    activeTrackColor: Color,
    inactiveTickColor: Color,
    activeTickColor: Color
) {
    val isRtl = layoutDirection == LayoutDirection.Rtl
    val sliderLeft = Offset(0f, center.y)
    val sliderRight = Offset(size.width, center.y)
    val sliderStart = if (isRtl) sliderRight else sliderLeft
    val sliderEnd = if (isRtl) sliderLeft else sliderRight
    val tickSize = TickSize.toPx()
    val trackStrokeWidth = TrackHeight.toPx()
    drawLine(
        inactiveTrackColor,
        sliderStart,
        sliderEnd,
        trackStrokeWidth,
        StrokeCap.Round
    )
    val sliderValueEnd = Offset(
        sliderStart.x +
                (sliderEnd.x - sliderStart.x) * activeRangeEnd,
        center.y
    )

    val sliderValueStart = Offset(
        sliderStart.x +
                (sliderEnd.x - sliderStart.x) * activeRangeStart,
        center.y
    )

    drawLine(
        activeTrackColor,
        sliderValueStart,
        sliderValueEnd,
        trackStrokeWidth,
        StrokeCap.Round
    )

    for (tick in tickFractions) {
        val outsideFraction = tick > activeRangeEnd || tick < activeRangeStart
        drawCircle(
            color = if (outsideFraction) inactiveTickColor else activeTickColor,
            center = Offset(
                androidx.compose.ui.geometry.lerp(sliderStart, sliderEnd, tick).x,
                center.y
            ),
            radius = tickSize / 2f
        )
    }
}


@Composable
fun Thumb(
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier,
    colors: SliderColors = colors(),
    enabled: Boolean = true,
    thumbSize: DpSize = ThumbSize
) {
    val interactions = remember { mutableStateListOf<Interaction>() }
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> interactions.add(interaction)
                is PressInteraction.Release -> interactions.remove(interaction.press)
                is PressInteraction.Cancel -> interactions.remove(interaction.press)
                is DragInteraction.Start -> interactions.add(interaction)
                is DragInteraction.Stop -> interactions.remove(interaction.start)
                is DragInteraction.Cancel -> interactions.remove(interaction.start)
            }
        }
    }

    val elevation = if (interactions.isNotEmpty()) {
        ThumbPressedElevation
    } else {
        ThumbDefaultElevation
    }
    val shape = CircleShape

    @Suppress("DEPRECATION_ERROR")
    (Spacer(
        modifier
            .size(thumbSize)
            .indication(
                interactionSource = interactionSource,
                indication = androidx.compose.material.ripple.rememberRipple(
                    bounded = false,
                    radius = StateLayerSize / 2
                )
            )
            .hoverable(interactionSource = interactionSource)
            .shadow(if (enabled) elevation else 0.dp, shape, clip = false)
            .background(colors.thumbColor(enabled), shape)
    ))
}

@Stable
internal fun SliderColors.thumbColor(enabled: Boolean): Color =
    if (enabled) thumbColor else disabledThumbColor

@Stable
internal fun SliderColors.trackColor(enabled: Boolean, active: Boolean): Color =
    if (enabled) {
        if (active) activeTrackColor else inactiveTrackColor
    } else {
        if (active) disabledActiveTrackColor else disabledInactiveTrackColor
    }

@Stable
internal fun SliderColors.tickColor(enabled: Boolean, active: Boolean): Color =
    if (enabled) {
        if (active) activeTickColor else inactiveTickColor
    } else {
        if (active) disabledActiveTickColor else disabledInactiveTickColor
    }

@OptIn(ExperimentalMaterial3Api::class)
internal val SliderState.coercedValueAsFraction
    get() = calcFraction(
        valueRange.start,
        valueRange.endInclusive,
        value.coerceIn(valueRange.start, valueRange.endInclusive)
    )

private fun calcFraction(a: Float, b: Float, pos: Float) =
    (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)

@OptIn(ExperimentalMaterial3Api::class)
internal val SliderState.tickFractions: FloatArray
    get() = stepsToTickFractions(steps)


private fun stepsToTickFractions(steps: Int): FloatArray {
    return if (steps == 0) floatArrayOf() else FloatArray(steps + 2) { it.toFloat() / (steps + 1) }
}

private val HandleHeight = 20.0.dp
private val HandleWidth = 20.0.dp

private val StateLayerSize = 40.0.dp
internal val ThumbWidth = HandleWidth
private val ThumbHeight = HandleHeight
private val ThumbSize = DpSize(ThumbWidth, ThumbHeight)
private val ThumbDefaultElevation = 1.dp
private val ThumbPressedElevation = 6.dp
private val TickMarksContainerSize = 2.0.dp
private val TickSize = TickMarksContainerSize

// Internal to be referred to in tests
private val InactiveTrackHeight = 4.0.dp
private val TrackHeight = InactiveTrackHeight
