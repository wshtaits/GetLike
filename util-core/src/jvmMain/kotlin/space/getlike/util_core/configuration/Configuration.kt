package space.getlike.util_core.configuration

import io.ktor.server.application.Application
import io.ktor.server.routing.Routing

abstract class Configuration {

    internal fun internalPlugins(application: Application) =
        application.plugins()

    internal fun internalRouting(routing: Routing) =
        routing.routing()

    protected open fun Application.plugins() {
    }

    protected open fun Routing.routing() {
    }
}