package space.getlike.util_core

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import space.getlike.util_core.utils.saveableStateOf
import kotlin.reflect.KClass

abstract class CoreViewModel<TDependencies : Any, TState : Any, TRoute : Route>(
    bundle: Bundle,
    initialState: (TDependencies, TRoute) -> TState,
) : BaseViewModel<TDependencies>(
    savedStateHandle = bundle.savedStateHandle,
    rootBackStack = bundle.viewBundle.rootBackStack,
) {

    @Suppress("UNCHECKED_CAST")
    override val deps = bundle.viewBundle.appViewModel.internalDeps as TDependencies

    override val modalRouteFlow = bundle.viewBundle.appViewModel.internalModalRouteFlow

    @Suppress("UNCHECKED_CAST")
    protected val route = bundle.viewBundle.route as TRoute

    @Suppress("UNCHECKED_CAST")
    @OptIn(InternalSerializationApi::class)
    internal val mutableState by bundle.savedStateHandle.saveableStateOf(
        initialValue = { initialState(deps, route) },
        serializer = (bundle.viewBundle.navigation.stateClass as KClass<TState>).serializer(),
    )

    @PublishedApi
    internal val effectFlow = MutableSharedFlow<Any>()

    internal val childBackStack = bundle.childBackStack

    protected var state: TState
        get() = mutableState.value
        set(value) { mutableState.value = value }

    private val parentViewModel = bundle.viewBundle.parentViewModel
    private val parentBackStack = bundle.viewBundle.parentBackStack

    init {
        viewModelScope.launch {
            closedRouteKey.collect { key ->
                if (key == route.key) {
                    cancelLaunchedJobs()
                }
            }
        }
    }

    internal fun internalOnSystemBackClick() =
        onSystemBackClick()

    internal fun internalOnModalDismiss() =
        onSystemBackClick()

    protected open fun onSystemBackClick() {
    }

    protected open fun onModalDismiss() {
    }

    protected fun perform(effect: Any) =
        launch { effectFlow.emit(effect) }

    protected suspend fun navigateBack(result: Any? = null) {
        launch { routeResultFlow.emit(route to result) }
        when {
            modalRouteFlow.value != null -> {
                modalRouteFlow.update { null }
                emitClosedRouteKey(modalRouteFlow.value)
            }
            parentBackStack.size > 1 -> {
                parentBackStack.removeAt(parentBackStack.lastIndex)
                emitClosedRouteKey(parentBackStack.lastOrNull())
            }
            parentViewModel is CoreViewModel<*, *, *> ->
                parentViewModel.onSystemBackClick()
        }
    }

    protected suspend fun navigateChild(
        route: Route,
        replace: Boolean = false,
        singleTop: Boolean = false,
        popAll: Boolean = false,
    ) =
        navigate(childBackStack, route, replace, singleTop, popAll)

    protected suspend fun navigateParent(
        route: Route,
        replace: Boolean = false,
        singleTop: Boolean = false,
        popAll: Boolean = false,
    ) =
        navigate(parentBackStack, route, replace, singleTop, popAll)

    class Bundle internal constructor(
        internal val viewBundle: View.Bundle,
        internal val savedStateHandle: SavedStateHandle,
        internal val childBackStack: NavBackStack<Route>,
    )
}