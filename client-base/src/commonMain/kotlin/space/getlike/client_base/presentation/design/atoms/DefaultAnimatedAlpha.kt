package space.getlike.client_base.presentation.design.atoms

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color

@Composable
fun DefaultAnimatedAlpha(
    modifier: Modifier = Modifier,
    visible: Boolean,
    content: @Composable () -> Unit,
) {
    val alpha by animateFloatAsState(
        targetValue = if (visible) {
            1f
        } else {
            0f
        },
    )
    Surface(
        modifier = modifier
            .alpha(alpha)
        ,
        color = Color.Transparent,
        content = content,
    )
}