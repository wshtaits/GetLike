package space.getlike.client_base.presentation.design.atoms

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import space.getlike.client_base.presentation.design.AppTheme

@Composable
fun DefaultSnackbarHost(
    modifier: Modifier,
    state: SnackbarHostState,
) {
    SnackbarHost(
        modifier = modifier,
        hostState = state,
        snackbar = { snackbarData ->
            Snackbar(
                snackbarData = snackbarData,
                containerColor = AppTheme.colors.secondary,
                contentColor = AppTheme.colors.background,
            )
        }
    )
}