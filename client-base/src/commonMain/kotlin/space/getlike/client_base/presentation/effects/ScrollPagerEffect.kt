package space.getlike.client_base.presentation.effects

import androidx.compose.animation.core.tween
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import space.getlike.util_core.View

data class ScrollPagerEffect(
    val page: Int,
) {

    companion object {

        @Composable
        context(view: View<*, *>)
        fun handle(pageCount: Int, animationDurationMillis: Int = 300): PagerState {
            val pagerState = rememberPagerState(pageCount = { pageCount })
            view.Handle<ScrollPagerEffect> {
                pagerState.animateScrollToPage(
                    page = page,
                    animationSpec = tween(animationDurationMillis),
                )
            }
            return pagerState
        }
    }
}