package space.getlike.client_base.presentation.design.atoms

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun DefaultScrollbar(
    modifier: Modifier,
    state: LazyListState,
    content: @Composable () -> Unit,
) {
    content()
}

@Composable
actual fun DefaultScrollbar(
    modifier: Modifier,
    state: ScrollState,
    content: @Composable () -> Unit,
) {
    content()
}
