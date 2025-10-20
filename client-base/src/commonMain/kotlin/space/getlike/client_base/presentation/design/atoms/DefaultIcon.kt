package space.getlike.client_base.presentation.design.atoms

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import space.getlike.client_base.presentation.design.AppTheme

@Composable
fun DefaultIcon(
    modifier: Modifier = Modifier,
    imageRes: DrawableResource,
    descriptionRes: StringResource,
    tint: Color = AppTheme.colors.secondary,
) {
    Icon(
        modifier = modifier,
        imageVector = vectorResource(imageRes),
        contentDescription = stringResource(descriptionRes),
        tint = tint,
    )
}