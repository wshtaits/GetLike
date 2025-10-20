package space.getlike.util_core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationEventHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import androidx.savedstate.serialization.SavedStateConfiguration
import io.ktor.util.reflect.instanceOf
import kotlin.reflect.KClass
import kotlin.uuid.ExperimentalUuidApi

abstract class View<TViewModel : CoreViewModel<*, TState, *>, TState : Any>(
    private val bundle: Bundle,
    private val viewModelFactory: (CoreViewModel.Bundle) -> TViewModel,
    private val initialChildBackStackRoute: Route = Route.Empty,
) {

    @Suppress("UNCHECKED_CAST")
    protected val state: TState
        get() = nullableViewModel?.mutableState?.value ?: bundle.exampleState as TState
    protected val viewModel by lazy { checkNotNull(nullableViewModel) }

    @PublishedApi
    internal var nullableViewModel: TViewModel? = null

    @OptIn(ExperimentalUuidApi::class, ExperimentalComposeUiApi::class)
    @Composable
    internal fun InternalUi() {
        if (LocalInspectionMode.current) {
            Ui()
            return
        }

        val viewModelKey = rememberSaveable { bundle.route.key }
        @Suppress("UNCHECKED_CAST")
        val childBackStack = rememberNavBackStack<Route>(
            SavedStateConfiguration.DEFAULT,
            initialChildBackStackRoute,
        ) as NavBackStack<Route>

        @Suppress("UNCHECKED_CAST")
        nullableViewModel = viewModel(
            key = viewModelKey,
            modelClass = bundle.navigation.viewModelClass as KClass<TViewModel>,
            factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T =
                    viewModelFactory(
                        CoreViewModel.Bundle(
                            viewBundle = bundle,
                            savedStateHandle = extras.createSavedStateHandle(),
                            childBackStack = childBackStack,
                        )
                    ) as T
            },
        )

        LaunchedEffect(Unit) {
            nullableViewModel?.internalOnLaunch()
        }

        NavigationEventHandler(
            state = rememberNavigationEventState<NavigationEventInfo>(NavigationEventInfo.None),
            isBackEnabled = !bundle.navigation.interceptSystemBack,
            onBackCompleted = { nullableViewModel?.internalOnSystemBackClick() }
        )

        Ui()
    }

    @Composable
    protected abstract fun Ui()

    @Composable
    inline fun <reified TEffect> Handle(
        crossinline block: suspend TEffect.() -> Unit
    ) {
        LaunchedEffect(Unit) {
            nullableViewModel?.effectFlow?.collect { effect ->
                if (effect is TEffect) {
                    block(effect)
                }
            }
        }
    }

    @Composable
    protected fun ChildNavDisplay(
        modifier: Modifier = Modifier,
        routing: List<Navigation>,
    ) {
        if (LocalInspectionMode.current) {
            Box(modifier = Modifier.fillMaxSize())
            return
        }

        NavDisplay(
            modifier = modifier,
            appViewModel = bundle.appViewModel,
            parentViewModel = viewModel,
            parentBackStack = viewModel.childBackStack,
            rootBackStack = bundle.rootBackStack,
            navigations = routing,
        )
    }

    companion object {

        fun fromNavigation(
            navigations: List<Navigation>,
            route: Route,
            appViewModel: CoreAppViewModel<*>,
            parentViewModel: BaseViewModel<*>,
            parentBackStack: NavBackStack<Route>,
            rootBackStack: NavBackStack<Route>,
        ): View<*, *> {
            val navigation = navigations
                .find { routeBundle -> route.instanceOf(routeBundle.routeClass) }
                ?: error("${route::class.simpleName} is not registered")
            val childViewBundle = Bundle(
                route = route,
                appViewModel = appViewModel,
                parentViewModel = parentViewModel,
                navigation = navigation,
                rootBackStack = rootBackStack,
                parentBackStack = parentBackStack,
                exampleState = null,
            )
            return navigation.viewFactory(childViewBundle)
        }
    }

    class Bundle internal constructor(
        route: Route?,
        navigation: Navigation?,
        appViewModel: CoreAppViewModel<*>?,
        parentViewModel: BaseViewModel<*>?,
        rootBackStack: NavBackStack<Route>?,
        parentBackStack: NavBackStack<Route>?,
        exampleState: Any?,
    ) {
        internal val route by lazy { checkNotNull(route) }
        internal val navigation by lazy { checkNotNull(navigation) }
        internal val appViewModel by lazy { checkNotNull(appViewModel) }
        internal val parentViewModel by lazy { checkNotNull(parentViewModel) }
        internal val rootBackStack by lazy { checkNotNull(rootBackStack) }
        internal val parentBackStack by lazy { checkNotNull(parentBackStack) }
        internal val exampleState by lazy { checkNotNull(exampleState) }
    }
}