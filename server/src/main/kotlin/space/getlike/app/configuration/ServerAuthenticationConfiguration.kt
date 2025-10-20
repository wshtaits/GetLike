package space.getlike.app.configuration

import io.ktor.server.application.Application
import io.ktor.server.application.install
import space.getlike.dependencies.Dependencies
import space.getlike.Urls
import space.getlike.util_core.configuration.Configuration
import space.getlike.util_authentication.ServerAuthenticationPlugin

class ServerAuthenticationConfiguration(
    private val deps: Dependencies,
) : Configuration() {

    override fun Application.plugins() {
        install(ServerAuthenticationPlugin) {
            authentication = deps.authentication
            excludedPaths = listOf(
                Urls.Paths.WellKnown.APPLE,
                Urls.Paths.WellKnown.ANDROID,
                Urls.Paths.NON_AUTH,
                Urls.Paths.AVATARS,
            )
        }
    }
}