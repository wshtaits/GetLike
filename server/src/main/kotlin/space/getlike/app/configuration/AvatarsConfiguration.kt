package space.getlike.app.configuration

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import space.getlike.data.Resources
import space.getlike.Urls
import space.getlike.util_core.configuration.Configuration

class AvatarsConfiguration : Configuration() {

    override fun Routing.routing() {
        get("${Urls.Paths.AVATARS}/{fileName}") {
            val fileName = call.parameters["fileName"]
            if (fileName.isNullOrEmpty()) {
                return@get call.respond(HttpStatusCode.BadRequest)
            }

            val file = Resources.Avatars.getFile(fileName)
            if (!file.exists()) {
                return@get call.respond(HttpStatusCode.NotFound)
            }

            call.respondFile(file)
        }
    }
}