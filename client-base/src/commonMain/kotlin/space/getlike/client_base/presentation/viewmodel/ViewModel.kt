package space.getlike.client_base.presentation.viewmodel

import kotlinx.coroutines.CoroutineScope
import space.getlike.client_base.dependencies.PresentationDependencies
import space.getlike.util_core.CoreViewModel
import space.getlike.util_core.Route
import space.getlike.util_logger.Logger

abstract class ViewModel<TState : Any, TRoute : Route>(
    val bundle: Bundle,
    private val analyticsName: String,
    initialState: (PresentationDependencies, TRoute) -> TState,
): CoreViewModel<PresentationDependencies, TState, TRoute>(bundle, initialState) {

    constructor(
        bundle: Bundle,
        analyticsName: String,
        initialState: TState,
    ) : this(
        bundle = bundle,
        analyticsName = analyticsName,
        initialState = { _, _ -> initialState },
    )

    override fun onLaunch() {
        logLaunchEvent()
    }

    override fun onSystemBackClick() = eventOnSystemBack {
        navigateBack()
    }

    override fun onModalDismiss() = eventOnModalDismiss {
        navigateBack()
    }

    protected fun eventOnLaunch(
        vararg params: Pair<String, Any>,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        logLaunchEvent(*params)
        launch(block = block)
    }

    protected fun eventOnSystemBack(
        vararg params: Pair<String, Any>,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        logSystemBackEvent(*params)
        launch(block = block)
    }

    protected fun eventOnModalDismiss(
        vararg params: Pair<String, Any>,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        logModalDismissEvent(*params)
        launch(block = block)
    }

    protected fun event(
        element: String,
        type: Logger.Element,
        action: Logger.Action,
        vararg params: Pair<String, Any>,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        logEvent(element, type, action, *params)
        launch(block = block)
    }

    protected fun logLaunchEvent(
        vararg params: Pair<String, Any>,
    ) {
        logEvent(
            element = analyticsName,
            type = Logger.Element.Screen,
            action = Logger.Action.Launch,
            params = params,
        )
    }

    protected fun logSystemBackEvent(
        vararg params: Pair<String, Any>,
    ) {
        logEvent(
            element = "SystemBack",
            type = Logger.Element.Button,
            action = Logger.Action.Click,
            params = params,
        )
    }

    protected fun logModalDismissEvent(
        vararg params: Pair<String, Any>,
    ) {
        logEvent(
            element = "ModalDismiss",
            type = Logger.Element.Screen,
            action = Logger.Action.Dismiss,
            params = params,
        )
    }

    protected fun logEvent(
        element: String,
        type: Logger.Element,
        action: Logger.Action,
        vararg params: Pair<String, Any>,
    ) {
        deps.logger.logEvent(
            screen = analyticsName,
            name = element,
            element = type,
            action = action,
            params = params.toMap(),
        )
    }

    protected fun logException(throwable: Throwable) =
        deps.logger.logException(throwable)

    protected companion object {

        val Field = Logger.Element.Field
        val Button = Logger.Element.Button
        val Action = Logger.Element.Action
        val KeyboardButton = Logger.Element.KeyboardButton
        val ListItem = Logger.Element.ListItem
        val Image = Logger.Element.Image
        val Menu = Logger.Element.Menu
        val MenuItem = Logger.Element.MenuItem

        val Click = Logger.Action.Click
        val Change = Logger.Action.Change
        val Dismiss = Logger.Action.Dismiss
        val Confirm = Logger.Action.Dismiss
        val Press = Logger.Action.Press
        val Release = Logger.Action.Release
    }
}