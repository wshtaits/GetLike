package space.getlike.util_core.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

fun Modifier.invisible(predicate: Boolean) =
    alpha(
        if (predicate) {
            0f
        } else {
            1f
        },
    )

@Composable
fun Modifier.rippleClickable(
    bounded: Boolean = true,
    radius: Dp = Dp.Unspecified,
    color: Color = Color.Unspecified,
    onClick: () -> Unit,
) =
    clickable(
        indication = ripple(bounded, radius, color),
        interactionSource = remember { MutableInteractionSource() },
        onClick = onClick,
    )

@Composable
fun Modifier.blinking(value: Boolean): Modifier {
    val infiniteTransition = rememberInfiniteTransition()

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
    )

    return alpha(
        if (value) {
            alpha
        } else {
            1f
        },
    )
}

@Composable
fun Modifier.applyIf(
    condition: Boolean,
    block: @Composable Modifier.() -> Modifier,
): Modifier =
    if (condition) {
        this.block()
    } else {
        this
    }

@Composable
fun <T> Modifier.applyIfNotNull(
    value: T?,
    block: @Composable Modifier.(T) -> Modifier,
): Modifier =
    if (value != null) {
        this.block(value)
    } else {
        this
    }