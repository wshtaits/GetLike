package space.getlike.util_core.configuration

import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngineFactory
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.routing.routing

fun <TEngine : ApplicationEngine, TConfiguration : ApplicationEngine.Configuration> embeddedServer(
    factory: ApplicationEngineFactory<TEngine, TConfiguration>,
    host: String,
    port: Int,
    configurations: List<Configuration>,
): EmbeddedServer<TEngine, TConfiguration> =
    io.ktor.server.engine.embeddedServer(
        factory = factory,
        host = host,
        port = port,
        module = {
            for (configuration in configurations) {
                configuration.internalPlugins(this)
            }
            routing {
                for (configuration in configurations) {
                    configuration.internalRouting(this)
                }
            }
        },
    )