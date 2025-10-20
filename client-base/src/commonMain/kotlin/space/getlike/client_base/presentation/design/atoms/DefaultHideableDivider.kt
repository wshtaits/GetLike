package space.getlike.client_base.presentation.design.atoms

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.design.other.OffsetBehavior
import space.getlike.util_core.utils.invoke

@Composable
fun DefaultTopHideableHorizontalDivider(
    modifier: Modifier = Modifier,
    state: ScrollState,
    hideOffset: Dp = 0.dp,
) {
    DefaultHideableHorizontalDivider(
        modifier = modifier,
        offsetBehavior = OffsetBehavior.ScrollTop(state),
        hideOffset = hideOffset,
    )
}

@Composable
fun DefaultTopHideableHorizontalDivider(
    modifier: Modifier = Modifier,
    state: LazyListState,
    hideOffset: Dp = 0.dp,
) {
    DefaultHideableHorizontalDivider(
        modifier = modifier,
        offsetBehavior = OffsetBehavior.LazyListTop(state),
        hideOffset = hideOffset,
    )
}

@Composable
fun DefaultBottomHideableHorizonalDivider(
    modifier: Modifier = Modifier,
    state: ScrollState,
    hideOffset: Dp = 0.dp,
) {
    DefaultHideableHorizontalDivider(
        modifier = modifier,
        offsetBehavior = OffsetBehavior.ScrollBottom(state),
        hideOffset = hideOffset,
    )
}

@Composable
fun DefaultBottomHideableHorizonalDivider(
    modifier: Modifier = Modifier,
    state: LazyListState,
    hideOffset: Dp = 0.dp,
) {
    DefaultHideableHorizontalDivider(
        modifier = modifier,
        offsetBehavior = OffsetBehavior.LazyListBottom(state),
        hideOffset = hideOffset,
    )
}

@Composable
fun DefaultHideableHorizontalDivider(
    modifier: Modifier,
    offsetBehavior: OffsetBehavior,
    hideOffset: Dp,
) {
    val offset by offsetBehavior.rememberOffset()

    if (offset == Dp.Unspecified) {
        return
    }

    DefaultAnimatedVisibility(
        modifier = modifier,
        visible = offset > hideOffset,
        duration = 250,
        fade = true,
    ) {
        HorizontalDivider(
            color = AppTheme.colors.background,
        )
        HorizontalDivider(
            color = AppTheme.colors.secondary(0.05f),
        )
    }
}