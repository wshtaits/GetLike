package space.getlike.util_core

import androidx.compose.runtime.Composable

@Composable
fun <TState : Any> Preview(
    view: (View.Bundle) -> View<*, TState>,
    state: () -> TState,
) {
    Preview(
        view = view,
        state = state()
    )
}

@Composable
fun <TState : Any> Preview(
    view: (View.Bundle) -> View<*, TState>,
    state: TState,
) {
    val bundle = View.Bundle(
        route = null,
        navigation = null,
        appViewModel = null,
        parentViewModel = null,
        rootBackStack = null,
        parentBackStack = null,
        exampleState = state,
    )
    view(bundle).InternalUi()
}