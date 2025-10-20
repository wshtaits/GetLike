package space.getlike.client_base.presentation.design.atoms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import space.getlike.client_base.presentation.design.AppTheme

@Composable
fun DefaultDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<Pair<StringResource, () -> Unit>>,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd)
        ,
    ) {
        DropdownMenu(
            expanded = expanded,
            containerColor = AppTheme.colors.background,
            onDismissRequest = onDismissRequest,
        ) {
            for ((res, action) in items) {
                DropdownMenuItem(
                    text = { Text(stringResource(res)) },
                    onClick = action,
                )
            }
        }
    }
}