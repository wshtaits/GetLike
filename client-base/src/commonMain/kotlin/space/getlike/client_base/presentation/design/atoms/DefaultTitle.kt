package space.getlike.client_base.presentation.design.atoms

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import space.getlike.client_base.data.repositories.SyncRepository
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.resources.*

@Composable
fun DefaultTitle(
    modifier: Modifier = Modifier,
    textRes: StringResource,
    state: TitleState = TitleState.Idle,
) {
    DefaultTitle(
        modifier = modifier,
        text = stringResource(textRes),
        state = state,
    )
}

@Composable
fun DefaultTitle(
    modifier: Modifier = Modifier,
    text: String,
    state: TitleState = TitleState.Idle,
) {
    ConstraintLayout(
        modifier = modifier,
    ) {
        val (titleRef, stateRef) = createRefs()

        val transition = updateTransition(targetState = state)
        val animationDuration = 300

        val animatedContentColor by transition.animateColor(
            transitionSpec = { tween(durationMillis = animationDuration) },
            targetValueByState = { state ->
                when (state) {
                    is TitleState.Idle -> AppTheme.colors.secondary
                    is TitleState.Error -> AppTheme.colors.error
                    else -> AppTheme.colors.primary
                }
            },
        )

        Text(
            modifier = Modifier
                .constrainAs(titleRef) {
                    centerTo(parent)
                }
            ,
            text = text,
            color = animatedContentColor,
            style = AppTheme.typography.titleSmall,
        )

        transition.AnimatedContent(
            modifier = Modifier
                .constrainAs(stateRef) {
                    start.linkTo(titleRef.end, margin = 6.dp)
                }
            ,
            transitionSpec = {
                val fadeIn = fadeIn(
                    animationSpec = tween(animationDuration),
                )
                val slideIn = slideInVertically(
                    animationSpec = tween(animationDuration),
                    initialOffsetY = { fullHeight -> fullHeight / 6 },
                )
                val fadeOut = fadeOut(
                    animationSpec = tween(animationDuration),
                )
                val slideOut = slideOutVertically(
                    animationSpec = tween(animationDuration),
                    targetOffsetY = { fullHeight -> -fullHeight / 6 },
                )
                (slideIn + fadeIn) togetherWith (slideOut + fadeOut)
            }
        ) { targetState ->
            Box(
                modifier = Modifier
                    .width(16.dp)
                    .height(24.dp)
                ,
                contentAlignment = Alignment.Center,
            ) {
                when (targetState) {
                    is TitleState.Loading ->
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(16.dp)
                            ,
                            color = animatedContentColor,
                            strokeWidth = 3.dp,
                        )
                    is TitleState.Error ->
                        DefaultIcon(
                            imageRes = targetState.iconRes,
                            descriptionRes = targetState.descriptionRes,
                            tint = animatedContentColor,
                        )
                    else -> {
                        // no op
                    }
                }
            }
        }
    }
}

sealed interface TitleState {

    data object Idle : TitleState

    data object Loading : TitleState

    data class Error(
        val iconRes: DrawableResource,
        val descriptionRes: StringResource,
    ) : TitleState
}

fun SyncRepository.SyncState.toTitleState(): TitleState =
    when(this) {
        SyncRepository.SyncState.Connected -> TitleState.Idle
        SyncRepository.SyncState.Loading -> TitleState.Loading
        SyncRepository.SyncState.Disconnected -> TitleState.Error(
            iconRes = Res.drawable.ic_wifi_off,
            descriptionRes = Res.string.common_no_network,
        )
    }