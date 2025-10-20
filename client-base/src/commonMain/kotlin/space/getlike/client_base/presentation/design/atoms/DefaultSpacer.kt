package space.getlike.client_base.presentation.design.atoms

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DefaultSpacer(
    modifier: Modifier = Modifier,
    width: Dp = 0.dp,
    height: Dp = 0.dp,
) {
    Spacer(
        modifier = modifier
            .width(width)
            .height(height)
        ,
    )
}