package space.getlike.app.configuration

import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.Routing
import io.ktor.server.websocket.WebSockets
import kotlinx.serialization.json.Json
import space.getlike.Urls
import space.getlike.dependencies.Dependencies
import space.getlike.util_core.configuration.Configuration
import space.getlike.util_messaging.webSocket

class WebSocketConfiguration(
    private val deps: Dependencies,
) : Configuration() {

    override fun Application.plugins() {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }

    override fun Routing.routing() =
        webSocket(
            path = Urls.Paths.WEB_SOCKET,
            context = deps.messagingContext,
            handlers = emptyList(),
        )
}