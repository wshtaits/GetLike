package space.getlike.client_base.presentation.design.atoms

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import space.getlike.client_base.presentation.design.AppTheme

@Composable
fun DefaultTextButton(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    textColor: Color = AppTheme.colors.secondary,
    textStyle: TextStyle = AppTheme.typography.labelSmall,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit,
) {
    DefaultTextButton(
        modifier = modifier,
        contentPadding = contentPadding,
        enabled = enabled,
        loading = loading,
        onClick = onClick,
    ) {
        Text(
            text = text,
            color = textColor,
            style = textStyle.copy(textAlign = TextAlign.Center),
        )
    }
}

@Composable
fun DefaultTextButton(
    textRes: StringResource,
    modifier: Modifier = Modifier,
    textColor: Color = AppTheme.colors.secondary,
    textStyle: TextStyle = AppTheme.typography.labelSmall,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit,
) {
    DefaultTextButton(
        text = stringResource(textRes),
        modifier = modifier,
        textColor = textColor,
        textStyle = textStyle,
        contentPadding = contentPadding,
        enabled = enabled,
        loading = loading,
        onClick = onClick,
    )
}

@Composable
fun DefaultTextButton(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = AppTheme.colors.secondary,
    textStyle: TextStyle = AppTheme.typography.labelSmall,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit,
) {
    DefaultTextButton(
        modifier = modifier,
        contentPadding = contentPadding,
        enabled = enabled,
        loading = loading,
        onClick = onClick,
    ) {
        Text(
            text = text,
            color = textColor,
            style = textStyle.copy(textAlign = TextAlign.Center),
        )
    }
}

@Composable
private fun DefaultTextButton(
    modifier: Modifier,
    contentPadding: PaddingValues,
    enabled: Boolean,
    loading: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val animationDuration = 300
    TextButton(
        modifier = modifier,
        contentPadding = contentPadding,
        enabled = enabled,
        onClick = onClick,
    ) {
        AnimatedContent(
            targetState = loading,
            transitionSpec = {
                val fadeIn = fadeIn(
                    animationSpec = tween(animationDuration),
                )
                val slideIn = slideInHorizontally(
                    animationSpec = tween(animationDuration),
                    initialOffsetX = { fullWidth -> -fullWidth / 4 },
                )

                val fadeOut = fadeOut(
                    animationSpec = tween(animationDuration),
                )
                val slideOut = slideOutHorizontally(
                    animationSpec = tween(animationDuration),
                    targetOffsetX = { fullWidth -> fullWidth / 4 },
                )

                (slideIn + fadeIn) togetherWith (slideOut + fadeOut)
            }
        ) { targetState ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                if (targetState) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp),
                        color = AppTheme.colors.secondary,
                        strokeWidth = 2.dp,
                    )
                } else {
                    content()
                }
            }
        }
    }
}