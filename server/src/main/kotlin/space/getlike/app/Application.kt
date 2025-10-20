package space.getlike.app

import io.ktor.server.netty.Netty
import space.getlike.Urls
import space.getlike.app.configuration.AvatarsConfiguration
import space.getlike.app.configuration.ContentNegotiationConfiguration
import space.getlike.util_core.configuration.embeddedServer
import space.getlike.app.configuration.RpcConfiguration
import space.getlike.app.configuration.ServerAuthenticationConfiguration
import space.getlike.app.configuration.WebSocketConfiguration
import space.getlike.app.configuration.WellKnownConfiguration
import space.getlike.dependencies.Dependencies

fun main() {
    val deps = Dependencies()
    embeddedServer(
        factory = Netty,
        host = Urls.Segments.Server.HOST,
        port = Urls.Segments.Server.PORT,
        configurations = listOf(
            AvatarsConfiguration(),
            ContentNegotiationConfiguration(),
            WebSocketConfiguration(deps), // must be before RpcConfiguration
            RpcConfiguration(deps),
            ServerAuthenticationConfiguration(deps),
            WellKnownConfiguration(),
        )
    ).start(wait = true)
}