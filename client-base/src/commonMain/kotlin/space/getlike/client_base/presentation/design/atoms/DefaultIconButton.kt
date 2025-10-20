package space.getlike.client_base.presentation.design.atoms

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import space.getlike.client_base.presentation.design.AppTheme

@Composable
fun DefaultIconButton(
    modifier: Modifier = Modifier,
    imageRes: DrawableResource,
    descriptionRes: StringResource,
    tint: Color = AppTheme.colors.secondary,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
    ) {
        DefaultIcon(
            imageRes = imageRes,
            descriptionRes = descriptionRes,
            tint = tint,
        )
    }
}