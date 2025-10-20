package space.getlike.app.configuration

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import space.getlike.util_core.configuration.Configuration

class ContentNegotiationConfiguration : Configuration() {

    override fun Application.plugins() {
        install(ContentNegotiation) {
            json()
        }
    }
}