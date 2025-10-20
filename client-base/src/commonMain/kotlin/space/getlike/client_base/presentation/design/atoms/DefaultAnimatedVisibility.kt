package space.getlike.client_base.presentation.design.atoms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

@Composable
fun DefaultAnimatedVisibility(
    modifier: Modifier = Modifier,
    visible: Boolean,
    duration: Int = 400,
    delay: Int = 0,
    mode: TransitionMode = TransitionMode.Both,
    easing: Easing = FastOutSlowInEasing,
    fade: Boolean = false,
    scale: Boolean = false,
    scaleTransformOrigin: TransformOrigin = TransformOrigin.Center,
    expand: Boolean = false,
    expandAlignment: Alignment = Alignment.Center,
    slideHorizontally: Boolean = false,
    slideHorizontallyOffset: (fullWidth: Int) -> Int = { fullWidth -> fullWidth / 4 },
    slideVertically: Boolean = false,
    slideVerticallyOffset: (fullWidth: Int) -> Int = { fullWidth -> fullWidth / 4 },
    content: @Composable() AnimatedVisibilityScope.() -> Unit,
) {
    val (enter, exit) = rememberTransitions(
        duration,
        delay,
        mode,
        easing,
        fade,
        scale,
        scaleTransformOrigin,
        expand,
        expandAlignment,
        slideHorizontally,
        slideHorizontallyOffset,
        slideVertically,
        slideVerticallyOffset,
    )
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = enter,
        exit = exit,
        content = content,
    )
}

@Composable
fun <T> Transition<T>.DefaultAnimatedVisibility(
    modifier: Modifier = Modifier,
    visible: (T) -> Boolean,
    duration: Int = 400,
    delay: Int = 0,
    easing: Easing = FastOutSlowInEasing,
    mode: TransitionMode = TransitionMode.Both,
    fade: Boolean = false,
    scale: Boolean = false,
    scaleTransformOrigin: TransformOrigin = TransformOrigin.Center,
    expand: Boolean = false,
    expandAlignment: Alignment = Alignment.Center,
    slideHorizontally: Boolean = false,
    slideHorizontallyOffset: (fullWidth: Int) -> Int = { fullWidth -> fullWidth / 4 },
    slideVertically: Boolean = false,
    slideVerticallyOffset: (fullWidth: Int) -> Int = { fullWidth -> fullWidth / 4 },
    content: @Composable() AnimatedVisibilityScope.() -> Unit,
) {
    val (enter, exit) = rememberTransitions(
        duration,
        delay,
        mode,
        easing,
        fade,
        scale,
        scaleTransformOrigin,
        expand,
        expandAlignment,
        slideHorizontally,
        slideHorizontallyOffset,
        slideVertically,
        slideVerticallyOffset,
    )
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = enter,
        exit = exit,
        content = content,
    )
}

@Composable
private fun rememberTransitions(
    duration: Int,
    delay: Int,
    mode: TransitionMode,
    easing: Easing,
    fade: Boolean,
    scale: Boolean,
    scaleTransformOrigin: TransformOrigin,
    expand: Boolean,
    expandAlignment: Alignment,
    slideHorizontally: Boolean,
    slideHorizontallyOffset: (fullWidth: Int) -> Int,
    slideVertically: Boolean,
    slideVerticallyOffset: (fullWidth: Int) -> Int,
): Pair<EnterTransition, ExitTransition> = remember(
    duration,
    delay,
    mode,
    easing,
    fade,
    scale,
    scaleTransformOrigin,
    expand,
    expandAlignment,
    slideHorizontally,
    slideHorizontallyOffset,
    slideVertically,
    slideVerticallyOffset,
) {
    val floatAnimationSpec = tween<Float>(duration, delay, easing)
    val intSizeAnimationSpec = tween<IntSize>(duration, delay, easing)
    val intOffsetAnimationSpec = tween<IntOffset>(duration, delay, easing)

    var enter: EnterTransition = EnterTransition.None
    var exit: ExitTransition = ExitTransition.None

    if (fade) {
        enter += fadeIn(floatAnimationSpec)
        exit += fadeOut(floatAnimationSpec)
    }

    if (scale) {
        enter += scaleIn(
            animationSpec = floatAnimationSpec,
            transformOrigin = scaleTransformOrigin,
        )
        exit += scaleOut(
            animationSpec = floatAnimationSpec,
            transformOrigin = scaleTransformOrigin,
        )
    }

    if (expand) {
        enter += expandIn(
            animationSpec = intSizeAnimationSpec,
            expandFrom = expandAlignment,
        )
        exit += shrinkOut(
            animationSpec = intSizeAnimationSpec,
            shrinkTowards = expandAlignment,
        )
    }

    if (slideHorizontally) {
        enter += slideInHorizontally(
            animationSpec = intOffsetAnimationSpec,
            initialOffsetX = slideHorizontallyOffset,
        )
        exit += slideOutHorizontally(
            animationSpec = intOffsetAnimationSpec,
            targetOffsetX = slideHorizontallyOffset,
        )
    }

    if (slideVertically) {
        enter += slideInVertically(
            animationSpec = intOffsetAnimationSpec,
            initialOffsetY = slideVerticallyOffset,
        )
        exit += slideOutVertically(
            animationSpec = intOffsetAnimationSpec,
            targetOffsetY = slideVerticallyOffset,
        )
    }

    enter = if (mode == TransitionMode.OnlyOut) {
        EnterTransition.None
    } else {
        enter
    }
    exit = if (mode == TransitionMode.OnlyIn) {
        ExitTransition.None
    } else {
        exit
    }

    enter to exit
}

enum class TransitionMode {
    OnlyOut,
    OnlyIn,
    Both,
}
