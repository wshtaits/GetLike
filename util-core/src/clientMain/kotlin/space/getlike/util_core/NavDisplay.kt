package space.getlike.util_core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay

@Composable
internal fun NavDisplay(
    modifier: Modifier = Modifier,
    appViewModel: CoreAppViewModel<*>,
    parentViewModel: BaseViewModel<*>,
    parentBackStack: NavBackStack<Route>,
    rootBackStack: NavBackStack<Route>,
    navigations: List<Navigation>,
) {
    if (parentBackStack.isEmpty()) {
        return
    }

    NavDisplay(
        modifier = modifier,
        backStack = parentBackStack,
        entryProvider = { route ->
            NavEntry(route) {
                if (route is Route.Empty) {
                    Box(modifier = Modifier.fillMaxSize())
                    return@NavEntry
                }

                val view = View.fromNavigation(
                    navigations = navigations,
                    route = route,
                    appViewModel = appViewModel,
                    parentViewModel = parentViewModel,
                    parentBackStack = parentBackStack,
                    rootBackStack = rootBackStack,
                )

                view.InternalUi()
            }
        }
    )
}
