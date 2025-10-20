package space.getlike.util_core

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import kotlinx.coroutines.CoroutineScope
import space.getlike.util_core.utils.saveableStateFlow

open class CoreAppViewModel<TDependencies : Any>(bundle: Bundle) : BaseViewModel<TDependencies>(
    savedStateHandle = bundle.savedStateHandle,
    rootBackStack = bundle.rootBackStack,
) {

    @Suppress("UNCHECKED_CAST")
    internal val internalDeps = bundle.dependenciesFactory(viewModelScope) as TDependencies

    internal val internalModalRouteFlow by savedStateHandle.saveableStateFlow<Route?>(null)

    override val deps = internalDeps

    override val modalRouteFlow = internalModalRouteFlow

    class Bundle(
        internal val savedStateHandle: SavedStateHandle,
        internal val dependenciesFactory: (CoroutineScope) -> Any,
        internal val rootBackStack: NavBackStack<Route>,
    )
}