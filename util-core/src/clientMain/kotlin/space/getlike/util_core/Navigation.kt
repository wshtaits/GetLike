package space.getlike.util_core

import kotlin.reflect.KClass

inline infix fun <
        reified TRoute : Route,
        reified TState : Any,
        reified TViewModel : CoreViewModel<*, TState, TRoute>,
> KClass<TRoute>.navigatesTo(
    noinline viewFactory: (View.Bundle) -> View<TViewModel, TState>,
): Navigation =
    Navigation(
        routeClass = TRoute::class,
        stateClass = TState::class,
        viewModelClass = TViewModel::class,
        viewFactory = viewFactory,
        interceptSystemBack = false,
    )

inline infix fun <
        reified TRoute : Route,
        reified TState : Any,
        reified TViewModel : CoreViewModel<*, TState, TRoute>,
> KClass<TRoute>.navigatesTo(
    params: NavigationParams<TRoute, TState, TViewModel>,
): Navigation =
    Navigation(
        routeClass = TRoute::class,
        stateClass = TState::class,
        viewModelClass = TViewModel::class,
        viewFactory = params.viewFactory,
        interceptSystemBack = params.interceptSystemBack,
    )

fun <
        TRoute : Route,
        TState : Any,
        TViewModel : CoreViewModel<*, TState, TRoute>,
> ((View.Bundle) -> View<TViewModel, TState>).parametrized(
    interceptSystemBack: Boolean,
): NavigationParams<TRoute, TState, TViewModel> =
    NavigationParams(
        viewFactory = this,
        interceptSystemBack = interceptSystemBack,
    )

fun <
        TRoute : Route,
        TState : Any,
        TViewModel : CoreViewModel<*, TState, TRoute>,
> ((View.Bundle) -> View<TViewModel, TState>).interceptSystemBack(): NavigationParams<TRoute, TState, TViewModel> =
    NavigationParams(
        viewFactory = this,
        interceptSystemBack = true
    )

class NavigationParams<TRoute : Route, TState : Any, TViewModel : CoreViewModel<*, TState, TRoute>>(
    val viewFactory: (View.Bundle) -> View<TViewModel, TState>,
    val interceptSystemBack: Boolean,
)

data class Navigation(
    internal val routeClass: KClass<out Route>,
    internal val stateClass: KClass<out Any>,
    internal val viewModelClass: KClass<out CoreViewModel<*, *, *>>,
    internal val viewFactory: (View.Bundle) -> View<*, *>,
    internal val interceptSystemBack: Boolean,
)