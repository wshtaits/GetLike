package space.getlike.client_base.presentation.design.molecules

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.design.atoms.DefaultAnimatedVisibility
import space.getlike.client_base.presentation.design.atoms.DefaultIcon
import space.getlike.client_base.presentation.design.atoms.DefaultSpacer
import space.getlike.client_base.presentation.design.atoms.TransitionMode
import space.getlike.resources.*
import space.getlike.util_core.utils.rememberMutableIntStateOf
import space.getlike.util_core.utils.rememberMutableStateOf
import space.getlike.util_core.utils.toDp
import space.getlike.util_core.utils.toPx
import space.getlike.utils.separatedByThreeChars

@Composable
fun LikeButton(
    modifier: Modifier,
    maxLikesCount: Int,
    onConfirm: (Int) -> Unit,
) {
    var isRealButtonVisible by rememberMutableStateOf(true)

    val transition = updateTransition(isRealButtonVisible)

    LaunchedEffect(transition.isRunning) {
        if (!transition.isRunning && !transition.currentState) {
            isRealButtonVisible = true
        }
    }

    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val slideOutOffset = remember {
        val screenWidth = windowInfo.containerSize.width
        val paddingEndPx = with(density) { 16.dp.toPx() }
        (screenWidth - paddingEndPx).toInt()
    }

    transition.DefaultAnimatedVisibility(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 24.dp)
        ,
        visible = { isRealButtonVisible -> !isRealButtonVisible },
        easing = CubicBezierEasing(1.0f, 0.0f, 0.5f, 1.0f),
        mode = TransitionMode.OnlyIn,
        scale = true,
        scaleTransformOrigin = TransformOrigin(1f, 1f),
    ) {
        FakeLikeButton()
    }

    transition.DefaultAnimatedVisibility(
        modifier = modifier,
        visible = { isRealButtonVisible -> isRealButtonVisible },
        easing = CubicBezierEasing(0.5f, 0.0f, 1.0f, 0.5f),
        mode = TransitionMode.OnlyOut,
        slideHorizontally = true,
        slideHorizontallyOffset = { -slideOutOffset },
    ) {
        RealLikeButton(
            maxLikesCount = maxLikesCount,
            onConfirm = { likesCount ->
                isRealButtonVisible = false
                onConfirm(likesCount)
            },
        )
    }
}

@Composable
private fun FakeLikeButton() {
    Box(
        modifier = Modifier
            .size(width = 166.dp, height = 60.dp)
            .background(
                color = AppTheme.colors.primary,
                shape = RoundedCornerShape(
                    topStart = 30.dp,
                    topEnd = 30.dp,
                    bottomStart = 30.dp,
                    bottomEnd = 0.dp,
                )
            )
            .padding(horizontal = 16.dp)
        ,
    ) {
        DefaultIcon(
            modifier = Modifier
                .align(Alignment.CenterStart)
            ,
            imageRes = Res.drawable.ic_double_arrow,
            descriptionRes = Res.string.like_button_cancel,
            tint = AppTheme.colors.background,
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 32.dp)
            ,
            text = stringResource(Res.string.like_button_swipe),
            color = AppTheme.colors.background,
            style = AppTheme.typography.buttonLarge,
        )

        DefaultIcon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
            ,
            imageRes = Res.drawable.ic_heart_filled,
            descriptionRes = Res.string.like_button_like_description,
            tint = AppTheme.colors.background,
        )
    }
}

@Composable
private fun RealLikeButton(
    maxLikesCount: Int,
    onConfirm: (Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val hapticProvider = hapticProvider()

    val screenWidthPx = LocalWindowInfo.current.containerSize.width

    val initialButtonWidthDp = 134.dp
    val initialButtonWidthPx = initialButtonWidthDp.toPx()
    val freeSpacePx = screenWidthPx - initialButtonWidthPx - 64.dp.toPx()
    val dragWidthPx = remember { Animatable(0f) }
    val buttonWidth = initialButtonWidthDp + dragWidthPx.value.toDp()

    var zone by rememberMutableStateOf(LikeButtonZone.Idle to LikeButtonZone.Idle)
    val likeZoneOffsetPx = 24.dp.toPx()
    val cancelZoneOffsetPx = 120.dp.toPx()

    val frozenMaxLikesCount = remember { maxLikesCount }
    var likesCount by rememberMutableIntStateOf(0)
    var likeZoneProgress by rememberMutableStateOf<Float?>(null)

    val backgroundColor by animateColorAsState(
        if (zone.second == LikeButtonZone.Cancel) {
            AppTheme.colors.error
        } else {
            AppTheme.colors.primary
        },
    )

    LaunchedEffect(likeZoneProgress != null) {
        scope.launch {
            while (isActive && likeZoneProgress != null && likesCount < frozenMaxLikesCount) {
                likesCount++
                val boundedLikeZoneProgress = likeZoneProgress?.coerceAtMost(0.99f) ?: break
                val interval = 250 * (1f - boundedLikeZoneProgress)
                delay(interval.toLong())
            }
        }
    }

    LaunchedEffect(zone) {
        when {
            zone.first == LikeButtonZone.Idle && zone.second == LikeButtonZone.Like ->
                hapticProvider.performHaptic()
            zone.first == LikeButtonZone.Like && zone.second == LikeButtonZone.Cancel ->
                hapticProvider.performHaptic()
        }
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .draggable(
                state = rememberDraggableState { delta ->
                    val value = (dragWidthPx.value - delta)
                        .coerceAtLeast(0f)
                        .coerceAtMost(freeSpacePx)

                    zone = zone.second to when {
                        value > cancelZoneOffsetPx -> LikeButtonZone.Cancel
                        value > likeZoneOffsetPx -> LikeButtonZone.Like
                        else -> LikeButtonZone.Idle
                    }

                    likeZoneProgress = if (zone.second == LikeButtonZone.Like) {
                        (value - likeZoneOffsetPx) / (cancelZoneOffsetPx - likeZoneOffsetPx)
                    } else {
                        null
                    }

                    scope.launch { dragWidthPx.snapTo(value) }
                },
                onDragStopped = {
                    likeZoneProgress = null

                    if (zone.second == LikeButtonZone.Like && likesCount != 0) {
                        onConfirm(likesCount)
                    } else {
                        likesCount = 0
                        zone = zone.second to LikeButtonZone.Idle
                        scope.launch {
                            dragWidthPx.animateTo(
                                targetValue = 0f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                            )
                        }
                    }
                },
                orientation = Orientation.Horizontal,
            )
            .height(60.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(
                    topStart = 30.dp,
                    topEnd = 30.dp,
                    bottomStart = 30.dp,
                    bottomEnd = 0.dp,
                )
            )
            .padding(horizontal = 16.dp)
        ,
    ) {
        DefaultSpacer(width = buttonWidth)

        Crossfade(
            modifier = Modifier
                .align(Alignment.CenterStart)
            ,
            targetState = zone.second == LikeButtonZone.Cancel,
        ) { targetState ->
            val (iconRes, descriptionRes) = if (targetState) {
                Res.drawable.ic_close to Res.string.like_button_swipe
            } else {
                Res.drawable.ic_double_arrow to Res.string.like_button_cancel
            }
            DefaultIcon(
                imageRes = iconRes,
                descriptionRes = descriptionRes,
                tint = AppTheme.colors.background,
            )
        }

        DefaultAnimatedVisibility(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 32.dp)
            ,
            visible = zone.second == LikeButtonZone.Idle,
            fade = true,
            slideHorizontally = true,
            slideHorizontallyOffset = { fullWidth -> -fullWidth / 4 },
        ) {
            Text(
                text = stringResource(Res.string.like_button_swipe),
                color = AppTheme.colors.background,
                style = AppTheme.typography.buttonLarge,
            )
        }

        DefaultAnimatedVisibility(
            modifier = Modifier
                .align(Alignment.CenterEnd)
            ,
            visible = zone.second != LikeButtonZone.Cancel,
            slideHorizontally = true,
            fade = true,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DefaultIcon(
                    imageRes = Res.drawable.ic_heart_filled,
                    descriptionRes = Res.string.like_button_like_description,
                    tint = AppTheme.colors.background,
                )

                DefaultAnimatedVisibility(
                    visible = zone.second != LikeButtonZone.Idle,
                    fade = true,
                    expand = true,
                    scale = true,
                    slideHorizontally = true,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        DefaultSpacer(width = 8.dp)

                        Text(
                            text = if (likesCount == frozenMaxLikesCount) {
                                stringResource(Res.string.like_button_max)
                            } else {
                                likesCount.separatedByThreeChars()
                            },
                            color = AppTheme.colors.background,
                            style = AppTheme.typography.titleMedium,
                        )
                    }
                }
            }
        }

        DefaultAnimatedVisibility(
            modifier = Modifier
                .align(Alignment.CenterEnd)
            ,
            visible = zone.second == LikeButtonZone.Cancel,
            fade = true,
            slideHorizontally = true,
            slideHorizontallyOffset = { fullWidth -> -fullWidth / 4 },
        ) {
            Text(
                text = stringResource(Res.string.like_button_cancel),
                color = AppTheme.colors.background,
                style = AppTheme.typography.buttonLarge,
            )
        }
    }
}

private enum class LikeButtonZone {
    Idle,
    Like,
    Cancel,
}

expect class HapticProvider

@Composable
expect fun hapticProvider(): HapticProvider

expect fun HapticProvider.performHaptic()