package space.getlike.client_base.presentation.design.other

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import space.getlike.util_core.utils.rememberDerivedStateOf

sealed class OffsetBehavior(private val key: Any) {

    @Composable
    fun rememberOffset(): State<Dp> {
        val density = LocalDensity.current
        return rememberDerivedStateOf(key, density) {
            with(density) {
                val offsetPx = offsetPx()
                when (offsetPx) {
                    Int.MIN_VALUE -> Dp.Unspecified
                    Int.MAX_VALUE -> Dp.Infinity
                    else -> offsetPx.toDp()
                }
            }
        }
    }

    protected abstract fun offsetPx(): Int

    data class ScrollTop(
        private val state: ScrollState,
    ) : OffsetBehavior(state) {

        override fun offsetPx(): Int =
            state.value
    }

    data class ScrollBottom(
        private val state: ScrollState,
    ) : OffsetBehavior(state) {

        override fun offsetPx(): Int =
            if (state.maxValue == Int.MAX_VALUE) {
                Int.MIN_VALUE
            } else {
                state.maxValue - state.value
            }
    }

    data class LazyListTop(
        private val state: LazyListState,
    ) : OffsetBehavior(state) {

        override fun offsetPx(): Int =
            if (state.firstVisibleItemIndex == 0) {
                state.firstVisibleItemScrollOffset
            } else {
                Int.MAX_VALUE
            }
    }

    data class LazyListBottom(
        private val state: LazyListState,
    ) : OffsetBehavior(state) {

        override fun offsetPx(): Int {
            val layoutInfo = state.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return Int.MIN_VALUE
            return if (lastVisibleItem.index == layoutInfo.totalItemsCount - 1) {
                (lastVisibleItem.offset + lastVisibleItem.size) - layoutInfo.viewportEndOffset
            } else {
                Int.MAX_VALUE
            }
        }
    }
}