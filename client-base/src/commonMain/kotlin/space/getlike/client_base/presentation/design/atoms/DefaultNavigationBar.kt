package space.getlike.client_base.presentation.design.atoms

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.util_core.utils.rippleClickable

@Composable
fun DefaultNavigationBar(
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(64.dp)
        ,
        containerColor = AppTheme.colors.background,
        content = content,
    )
}

@Composable
fun RowScope.DefaultNavigationItem(
    selected: Boolean,
    selectedIconRes: DrawableResource,
    unselectedIconRes: DrawableResource,
    descriptionRes: StringResource,
    onClick: () -> Unit,
) {
    DefaultNavigationItem(
        icon = {
            DefaultNavigationItemIcon(
                selected,
                selectedIconRes,
                unselectedIconRes,
                descriptionRes,
            )
        },
        onClick = onClick,
    )
}

@Composable
fun RowScope.DefaultNavigationItem(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .rippleClickable(
                color = AppTheme.colors.primary,
                radius = 28.dp,
                onClick = onClick,
            )
        ,
        contentAlignment = Alignment.Center,
        content = { icon() },
    )
}

@Composable
fun DefaultNavigationItemIcon(
    selected: Boolean,
    selectedIconRes: DrawableResource,
    unselectedIconRes: DrawableResource,
    descriptionRes: StringResource,
    animationDuration: Int = 200
) {
    val tint by animateColorAsState(
        if (selected) {
            AppTheme.colors.primary
        } else {
            AppTheme.colors.secondary
        },
        animationSpec = tween(animationDuration)
    )
    Crossfade(
        targetState = selected,
        animationSpec = tween(animationDuration)
    ) { targetState ->
        DefaultIcon(
            imageRes = if (targetState) {
                selectedIconRes
            } else {
                unselectedIconRes
            },
            descriptionRes = descriptionRes,
            tint = tint,
        )
    }
}