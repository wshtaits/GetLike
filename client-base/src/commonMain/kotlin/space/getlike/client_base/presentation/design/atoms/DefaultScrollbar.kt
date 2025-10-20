package space.getlike.client_base.presentation.design.atoms

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun DefaultScrollbar(
    modifier: Modifier = Modifier,
    state: LazyListState,
    content: @Composable () -> Unit,
)

@Composable
expect fun DefaultScrollbar(
    modifier: Modifier = Modifier,
    state: ScrollState,
    content: @Composable () -> Unit,
)
