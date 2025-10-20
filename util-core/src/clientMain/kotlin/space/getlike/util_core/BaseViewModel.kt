package space.getlike.util_core

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import space.getlike.util_core.utils.saveableMutableSetOf

abstract class BaseViewModel<TDependencies : Any> internal constructor(
    internal val savedStateHandle: SavedStateHandle,
    internal val rootBackStack: NavBackStack<Route>,
) : ViewModel() {

    protected abstract val deps: TDependencies

    protected abstract val modalRouteFlow: MutableStateFlow<Route?>

    @PublishedApi
    internal val openedRouteKeys by savedStateHandle.saveableMutableSetOf<String>()

    private val launchedJobs = mutableListOf<Job>()

    internal fun internalOnLaunch() {
        if (savedStateHandle.get<Boolean>(KEY_IS_RESTORED) != true) {
            onLaunch()
            savedStateHandle[KEY_IS_RESTORED] = true
        }
    }

    protected open fun onLaunch() {
    }

    protected fun navigateModal(route: Route) {
        modalRouteFlow.update { route }
    }

    protected suspend fun navigateRoot(
        route: Route,
        replace: Boolean = false,
        singleTop: Boolean = false,
        popAll: Boolean = false,
    ) =
        navigate(rootBackStack, route, replace, singleTop, popAll)

    internal suspend fun navigate(
        backStack: NavBackStack<Route>,
        route: Route,
        replace: Boolean,
        singleTop: Boolean,
        popAll: Boolean,
    ) {
        var finalRoute: Route = route
        val closedKeys = mutableSetOf<Route?>()

        if (modalRouteFlow.value != null) {
            closedKeys.add(modalRouteFlow.value)
            modalRouteFlow.update { null }
        }

        if (backStack.getOrNull(0) is Route.Empty) {
            backStack.removeAt(0)
        }

        if (replace && backStack.isNotEmpty()) {
            closedKeys.add(backStack.lastOrNull())
            backStack.removeAt(backStack.lastIndex)
        }

        if (singleTop) {
            finalRoute = backStack
                .find { backStackRoute -> backStackRoute::class == route::class }
                as? Route
                ?: route
            backStack.removeAll { backStackRoute -> backStackRoute::class == route::class }
        }

        if (popAll) {
            closedKeys.addAll(backStack)
            backStack.clear()
        }

        openedRouteKeys.add(finalRoute.key)
        backStack.add(finalRoute)

        emitClosedRouteKeys(closedKeys)
    }

    internal suspend fun emitClosedRouteKey(route: Route?) =
        emitClosedRouteKeys(setOf(route))

    internal suspend fun emitClosedRouteKeys(routes: Collection<Route?>) {
        for (route in routes) {
            val key = route?.key ?: continue
            closedRouteKey.emit(key)
        }
    }

    protected inline fun <reified TRoute : Route> onResult(
        crossinline action: suspend (result: Any?) -> Unit,
    ) {
        internalOnResult<TRoute> { result ->
            action(result)
        }
    }

    protected inline fun <reified TRoute : Route> onResult(
        crossinline action: suspend () -> Unit,
    ) {
        internalOnResult<TRoute> { result ->
            action()
        }
    }

    @PublishedApi
    internal inline fun <reified TRoute : Route> internalOnResult(
        crossinline action: suspend (result: Any?) -> Unit,
    ) {
        viewModelScope.launch {
            routeResultFlow.collect { (route, result) ->
                if (route !is TRoute) {
                    return@collect
                }

                val isRemoved = openedRouteKeys.removeAll { openedRouteKey -> openedRouteKey == route.key }
                if (isRemoved) {
                    action(result)
                }
            }
        }
    }

    protected fun <T> Flow<T>.launchCollect(collector: suspend (T) -> Unit): Job =
        launch { collect(collector) }

    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        val job = viewModelScope.launch(block = block)
        launchedJobs.add(job)
        return job
    }

    internal fun cancelLaunchedJobs() {
        for (job in launchedJobs) {
            job.cancel()
        }
    }

    companion object {

        @PublishedApi
        internal val routeResultFlow = MutableSharedFlow<Pair<Route, Any?>>()

        @PublishedApi
        internal val closedRouteKey = MutableSharedFlow<String>()

        private const val KEY_IS_RESTORED = "isRestored"
    }
}