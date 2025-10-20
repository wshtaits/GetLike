package space.getlike.client_base.presentation.design.atoms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.design.other.OffsetBehavior
import space.getlike.util_core.utils.rememberMutableStateOf

@Composable
fun DefaultTopAppBar(
    titleRes: StringResource?,
    state: TitleState = TitleState.Idle,
    offsetBehavior: OffsetBehavior? = null,
    dividerHideOffset: Dp = 0.dp,
    removeWindowInsets: Boolean = false,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    DefaultTopAppBar(
        title = titleRes?.let { stringResource(it) },
        state = state,
        offsetBehavior = offsetBehavior,
        dividerHideOffset = dividerHideOffset,
        removeWindowInsets = removeWindowInsets,
        navigationIcon = navigationIcon,
        actions = actions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    title: String? = null,
    state: TitleState = TitleState.Idle,
    offsetBehavior: OffsetBehavior? = null,
    dividerHideOffset: Dp = 0.dp,
    removeWindowInsets: Boolean = false,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    DefaultTopAppBar(
        title = {
            if (title != null) {
                DefaultTitle(
                    text = title,
                    state = state,
                )
            }
        },
        offsetBehavior = offsetBehavior,
        dividerHideOffset = dividerHideOffset,
        removeWindowInsets = removeWindowInsets,
        navigationIcon = navigationIcon,
        actions = actions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    offsetBehavior: OffsetBehavior? = null,
    dividerHideOffset: Dp = 0.dp,
    removeWindowInsets: Boolean = false,
    title: @Composable (offset: Dp) -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    Box {
        CenterAlignedTopAppBar(
            modifier = Modifier
                .padding(top = 0.dp, start = 4.dp, end = 4.dp)
            ,
            windowInsets = if (removeWindowInsets) {
                WindowInsets(0)
            } else {
                TopAppBarDefaults.windowInsets
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = AppTheme.colors.background,
            ),
            navigationIcon = navigationIcon,
            title = {
                val offset by offsetBehavior?.rememberOffset() ?: rememberMutableStateOf(0.dp)
                title(offset)
            },
            actions = actions,
        )

        if (offsetBehavior != null) {
            DefaultHideableHorizontalDivider(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                ,
                offsetBehavior = offsetBehavior,
                hideOffset = dividerHideOffset,
            )
        }
    }
}