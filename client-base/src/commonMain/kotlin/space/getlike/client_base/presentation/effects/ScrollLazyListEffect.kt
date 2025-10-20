package space.getlike.client_base.presentation.effects

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import space.getlike.util_core.View

data class ScrollLazyListEffect(
    val side: Side = Side.Top,
) {

    companion object {

        @Composable
        context(view: View<*, *>)
        fun handle(): LazyListState {
            val pagerState = rememberLazyListState()
            view.Handle<ScrollLazyListEffect> {
                val index = when (side) {
                    Side.Top -> 0
                    Side.Bottom -> pagerState.layoutInfo.totalItemsCount - 1
                }
                pagerState.animateScrollToItem(index)
            }
            return pagerState
        }
    }

    enum class Side {
        Top,
        Bottom,
    }
}