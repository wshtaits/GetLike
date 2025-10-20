package space.getlike

import io.ktor.http.*
import space.getlike.util_core.models.Mime

object Urls {

    val BASE = "${URLProtocol.HTTPS.name}://${Segments.Client.HOST}"
    val WEB_SOCKET = "${URLProtocol.WSS.name}://${Segments.Client.HOST}${Paths.WEB_SOCKET}"

    fun avatar(fileName: String, mime: Mime): String =
        "$BASE${Paths.AVATARS}/$fileName.${mime.fileExtension}"

    object Segments {

        object Server {
            const val HOST = "localhost"
            const val PORT = 8080
        }

        object Client {
            val PROTOCOL = URLProtocol.WSS
            const val HOST = "rarely-settled-yeti.ngrok-free.app"
            const val PORT = 443
        }
    }

    object Paths {

        const val WEB_SOCKET = "/ws"
        const val NON_AUTH = "/non_auth"
        const val AVATARS = "/media/avatars"

        object WellKnown {
            const val BASE = "/.well-known"
            const val APPLE = "$BASE/apple-app-site-association"
            const val ANDROID = "$BASE/assetlinks.json"
        }
    }
}