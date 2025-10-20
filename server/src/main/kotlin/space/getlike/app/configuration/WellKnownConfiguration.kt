package space.getlike.app.configuration

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import space.getlike.Urls
import space.getlike.util_core.configuration.Configuration

class WellKnownConfiguration : Configuration() {

    override fun Routing.routing() {
        staticResources(Urls.Paths.WellKnown.BASE, "static${Urls.Paths.WellKnown.BASE}")
        get(Urls.Paths.WellKnown.APPLE) {
            val text = this::class.java.classLoader
                ?.getResource("static${Urls.Paths.WellKnown.APPLE}")?.readText()
            if (text != null) {
                call.respondText(text, ContentType.Application.Json)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}