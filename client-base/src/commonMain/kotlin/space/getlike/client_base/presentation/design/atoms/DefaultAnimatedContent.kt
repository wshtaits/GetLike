package space.getlike.client_base.presentation.design.atoms

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin

@Composable
fun <S> DefaultAnimatedContent(
    modifier: Modifier = Modifier,
    targetState: S,
    durationMillis: Int = 400,
    scale: Boolean = false,
    scaleTransformOrigin: TransformOrigin = TransformOrigin.Center,
    content: @Composable() AnimatedContentScope.(targetState: S) -> Unit,
) {
    var enterTransition = EnterTransition.None
    var exitTransition = ExitTransition.None

    if (scale) {
        enterTransition += scaleIn(
            animationSpec = tween(durationMillis = durationMillis / 2, delayMillis = durationMillis / 2),
            transformOrigin = scaleTransformOrigin,
        )
        exitTransition += scaleOut(
            animationSpec = tween(durationMillis = durationMillis / 2),
            transformOrigin = scaleTransformOrigin,
        )
    }

    AnimatedContent(
        modifier = modifier,
        targetState = targetState,
        contentAlignment = Alignment.Center,
        transitionSpec = { enterTransition togetherWith exitTransition },
        content = content,
    )
}