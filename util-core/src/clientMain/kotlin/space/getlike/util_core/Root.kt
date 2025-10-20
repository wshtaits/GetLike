package space.getlike.util_core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.rememberNavBackStack
import kotlinx.coroutines.CoroutineScope
import androidx.compose.runtime.collectAsState
import androidx.navigation3.runtime.NavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration

@Composable
fun <TDependencies : Any> Root(
    dependenciesFactory: (SavedStateHandle, CoroutineScope) -> TDependencies,
    appViewModelFactory: (CoreAppViewModel.Bundle) -> CoreAppViewModel<TDependencies>,
    initialRoute: Route = Route.Empty,
    navigations: List<Navigation>,
) {
    @Suppress("UNCHECKED_CAST")
    val rootBackStack = rememberNavBackStack<Route>(
        SavedStateConfiguration.DEFAULT,
        initialRoute,
    ) as NavBackStack<Route>
    val appViewModel = viewModel {
        val savedStateHandle = createSavedStateHandle()
        appViewModelFactory(
            CoreAppViewModel.Bundle(
                savedStateHandle = savedStateHandle,
                dependenciesFactory = { scope -> dependenciesFactory(savedStateHandle, scope) },
                rootBackStack = rootBackStack,
            ),
        )
    }

    LaunchedEffect(Unit) {
        appViewModel.internalOnLaunch()
    }

    NavDisplay(
        appViewModel = appViewModel,
        parentViewModel = appViewModel,
        parentBackStack = appViewModel.rootBackStack,
        rootBackStack = appViewModel.rootBackStack,
        navigations = navigations,
    )

    val modalRoute = appViewModel.internalModalRouteFlow.collectAsState().value
    if (modalRoute != null) {
        val view = remember {
            View.fromNavigation(
                navigations = navigations,
                route = modalRoute,
                appViewModel = appViewModel,
                parentViewModel = appViewModel,
                parentBackStack = rootBackStack,
                rootBackStack = rootBackStack,
            )
        }

        ModalBottomSheet(
            onDismissRequest = { view.nullableViewModel?.internalOnModalDismiss() },
        ) {
            view.InternalUi()
        }
    }
}
