package space.getlike.client_base.presentation.design.atoms

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import space.getlike.client_base.presentation.design.AppTheme

@Composable
fun DefaultLargeButton(
    modifier: Modifier = Modifier,
    visuals: ButtonVisuals,
    onClick: () -> Unit,
    onPress: () -> Unit = {},
    onRelease: () -> Unit = {},
) {
    DefaultLargeButton(
        modifier = modifier,
        iconRes = visuals.iconRes,
        iconDescription = visuals.iconDescription,
        textRes = visuals.textRes,
        color = visuals.color,
        enabled = visuals.enabled,
        loading = visuals.loading,
        onClick = onClick,
        onPress = onPress,
        onRelease = onRelease,
    )
}

@Composable
fun DefaultLargeButton(
    modifier: Modifier = Modifier,
    textRes: StringResource? = null,
    iconRes: DrawableResource? = null,
    iconDescription: StringResource? = null,
    color: Color = AppTheme.colors.primary,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit,
    onPress: () -> Unit = {},
    onRelease: () -> Unit = {},
) {
    DefaultButton(
        modifier = modifier,
        width = 212.dp,
        height = 60.dp,
        iconRes = iconRes,
        iconDescription = iconDescription,
        textRes = textRes,
        textStyle = AppTheme.typography.buttonLarge,
        color = color,
        loaderSize = 24.dp,
        enabled = enabled,
        loading = loading,
        onClick = onClick,
        onPress = onPress,
        onRelease = onRelease,
    )
}

@Composable
fun DefaultSmallButton(
    modifier: Modifier = Modifier,
    visuals: ButtonVisuals,
    onClick: () -> Unit,
    onPress: () -> Unit = {},
    onRelease: () -> Unit = {},
) {
    DefaultSmallButton(
        modifier = modifier,
        iconRes = visuals.iconRes,
        iconDescription = visuals.iconDescription,
        textRes = visuals.textRes,
        color = visuals.color,
        enabled = visuals.enabled,
        loading = visuals.loading,
        onClick = onClick,
        onPress = onPress,
        onRelease = onRelease,
    )
}

@Composable
fun DefaultSmallButton(
    modifier: Modifier = Modifier,
    textRes: StringResource? = null,
    iconRes: DrawableResource? = null,
    iconDescription: StringResource? = null,
    color: Color = AppTheme.colors.primary,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit,
    onPress: () -> Unit = {},
    onRelease: () -> Unit = {},
) {
    DefaultButton(
        modifier = modifier,
        width = 72.dp,
        height = 36.dp,
        iconRes = iconRes,
        iconDescription = iconDescription,
        textRes = textRes,
        textStyle = AppTheme.typography.buttonSmall,
        color = color,
        loaderSize = 20.dp,
        enabled = enabled,
        loading = loading,
        onClick = onClick,
        onPress = onPress,
        onRelease = onRelease,
    )
}

@Composable
private fun DefaultButton(
    modifier: Modifier,
    width: Dp,
    height: Dp,
    textRes: StringResource?,
    textStyle: TextStyle,
    iconRes: DrawableResource?,
    iconDescription: StringResource?,
    loaderSize: Dp,
    color: Color,
    enabled: Boolean,
    loading: Boolean,
    onClick: () -> Unit,
    onPress: () -> Unit,
    onRelease: () -> Unit,
) {
    val colorState = if (enabled) {
        ButtonColorState(
            contentColor = AppTheme.colors.background,
            containerColor = color,
            borderColor = Color.Transparent,
        )
    } else {
        ButtonColorState(
            contentColor = color,
            containerColor = AppTheme.colors.background,
            borderColor = color
        )
    }

    val contentState = ButtonContentState(
        textRes = textRes,
        loading = loading,
        iconRes = iconRes,
        iconDescription = iconDescription,
    )

    val animationDuration = 300

    val colorTransition = updateTransition(targetState = colorState)
    val animatedContentColor by colorTransition.animateColor(
        transitionSpec = { tween(durationMillis = animationDuration) },
        targetValueByState = { state -> state.contentColor },
    )
    val animatedContainerColor by colorTransition.animateColor(
        transitionSpec = { tween(durationMillis = animationDuration) },
        targetValueByState = { state -> state.containerColor },
    )
    val animatedBorderColor by colorTransition.animateColor(
        transitionSpec = { tween(durationMillis = animationDuration) },
        targetValueByState = { state -> state.borderColor },
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    LaunchedEffect(isPressed) {
        if (isPressed) {
            onPress()
        } else {
            onRelease()
        }
    }

    Button(
        modifier = modifier
            .size(width, height)
        ,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedContainerColor,
            contentColor = animatedContentColor,
            disabledContainerColor = animatedContainerColor,
            disabledContentColor = animatedContentColor,
        ),
        border = BorderStroke(
            width = 2.dp,
            color = animatedBorderColor,
        ),
        contentPadding = PaddingValues(0.dp),
        interactionSource = interactionSource,
        onClick = onClick,
    ) {
        AnimatedContent(
            targetState = contentState,
            transitionSpec = {
                val fadeIn = fadeIn(
                    animationSpec = tween(animationDuration),
                )
                val slideIn = slideInHorizontally(
                    animationSpec = tween(animationDuration),
                    initialOffsetX = { fullWidth -> fullWidth / 4 },
                )
                val fadeOut = fadeOut(
                    animationSpec = tween(animationDuration),
                )
                val slideOut = slideOutHorizontally(
                    animationSpec = tween(animationDuration),
                    targetOffsetX = { fullWidth -> -fullWidth / 4 },
                )
                (slideIn + fadeIn) togetherWith (slideOut + fadeOut)
            }
        ) { targetState ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                if (targetState.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(loaderSize)
                        ,
                        color = animatedContentColor,
                    )

                    if (targetState.iconRes != null || targetState.textRes != null) {
                        DefaultSpacer(width = 8.dp)
                    }
                }

                if (targetState.iconRes != null && targetState.iconDescription != null) {
                    DefaultIcon(
                        imageRes = targetState.iconRes,
                        descriptionRes = targetState.iconDescription,
                        tint = animatedContentColor,
                    )

                    if (targetState.textRes != null) {
                        DefaultSpacer(width = 8.dp)
                    }
                }

                if (targetState.textRes != null) {
                    Text(
                        text = stringResource(targetState.textRes),
                        style = textStyle,
                    )
                }
            }
        }
    }
}

@Composable
fun buttonVisuals(
    textRes: StringResource? = null,
    iconRes: DrawableResource? = null,
    iconDescription: StringResource? = null,
    color: Color = AppTheme.colors.primary,
    enabled: Boolean = true,
    loading: Boolean = false,
): ButtonVisuals =
    ButtonVisuals(
        textRes = textRes,
        iconRes = iconRes,
        iconDescription = iconDescription,
        color = color,
        enabled = enabled,
        loading = loading,
    )

data class ButtonVisuals(
    val textRes: StringResource?,
    val iconRes: DrawableResource?,
    val iconDescription: StringResource?,
    val color: Color,
    val enabled: Boolean,
    val loading: Boolean,
)

private data class ButtonColorState(
    val contentColor: Color,
    val containerColor: Color,
    val borderColor: Color,
)

private data class ButtonContentState(
    val textRes: StringResource?,
    val loading: Boolean,
    val iconRes: DrawableResource?,
    val iconDescription: StringResource?,
)