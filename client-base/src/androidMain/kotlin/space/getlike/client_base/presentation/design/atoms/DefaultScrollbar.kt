package space.getlike.client_base.presentation.design.atoms

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import my.nanihadesuka.compose.ColumnScrollbar
import my.nanihadesuka.compose.LazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSelectionMode
import my.nanihadesuka.compose.ScrollbarSettings
import space.getlike.client_base.presentation.design.AppColors
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.util_core.utils.invoke

@Composable
actual fun DefaultScrollbar(
    modifier: Modifier,
    state: LazyListState,
    content: @Composable () -> Unit,
) {
    LazyColumnScrollbar(
        modifier = modifier,
        state = state,
        settings = settings(AppTheme.colors),
        content = content,
    )
}

@Composable
actual fun DefaultScrollbar(
    modifier: Modifier,
    state: ScrollState,
    content: @Composable () -> Unit,
) {
    ColumnScrollbar(
        modifier = modifier,
        state = state,
        settings = settings(AppTheme.colors),
        content = content,
    )
}

private fun settings(colors: AppColors) = ScrollbarSettings.Default.copy(
    thumbThickness = 4.dp,
    scrollbarPadding = 0.dp,
    thumbUnselectedColor = colors.secondary(0.2f),
    thumbShape = RectangleShape,
    selectionMode = ScrollbarSelectionMode.Disabled,
    hideDelayMillis = 300,
    durationAnimationMillis = 300,
)
