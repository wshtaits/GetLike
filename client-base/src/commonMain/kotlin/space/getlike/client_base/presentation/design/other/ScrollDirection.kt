package space.getlike.client_base.presentation.design.other

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import space.getlike.util_core.utils.rememberMutableStateOf

@Composable
fun LazyListState.rememberScrollDirection(): State<ScrollDirection> {
    val scrollDirection = rememberMutableStateOf(ScrollDirection.NONE)

    LaunchedEffect(this) {
        snapshotFlow { firstVisibleItemIndex to firstVisibleItemScrollOffset }
            .distinctUntilChanged()
            .collect { (index, offset) ->
                val direction = when {
                    index > scrollDirection.value.ordinal -> ScrollDirection.DOWN
                    index < scrollDirection.value.ordinal -> ScrollDirection.UP
                    else -> when {
                        offset > 0 -> ScrollDirection.DOWN
                        offset < 0 -> ScrollDirection.UP
                        else -> ScrollDirection.NONE
                    }
                }
                scrollDirection.value = direction
            }
    }

    return scrollDirection
}

enum class ScrollDirection {
    UP,
    DOWN,
    NONE
}