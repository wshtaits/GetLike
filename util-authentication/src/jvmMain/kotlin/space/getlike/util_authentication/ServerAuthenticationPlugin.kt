package space.getlike.util_authentication

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.uri
import io.ktor.server.response.*
import io.ktor.util.*
import io.ktor.util.pipeline.PipelineContext

class ServerAuthenticationPlugin private constructor() {

    class Config(
        var authentication: Authentication? = null,
        var excludedPaths: List<String> = emptyList(),
    )

    companion object Plugin : BaseApplicationPlugin<ApplicationCallPipeline, Config, ServerAuthenticationPlugin> {

        override val key = AttributeKey<ServerAuthenticationPlugin>("AuthenticationPlugin")

        override fun install(
            pipeline: ApplicationCallPipeline,
            configure: Config.() -> Unit,
        ): ServerAuthenticationPlugin {
            val config = Config().apply(configure)
            val plugin = ServerAuthenticationPlugin()
            val authentication = config.authentication ?: return plugin

            pipeline.intercept(ApplicationCallPipeline.Plugins) {
                val isExcludedPath = config.excludedPaths
                    .any { excludedPath -> call.request.uri.startsWith(excludedPath) }
                if (isExcludedPath) {
                    return@intercept
                }

                val deviceId = call.request.headers[Header.DEVICE_ID]
                if (deviceId.isNullOrBlank()) {
                    finish("Empty device id")
                    return@intercept
                }

                val tokenId = call.request.headers[Header.TOKEN_ID]
                if (tokenId.isNullOrBlank()) {
                    finish("Empty token id")
                    return@intercept
                }

                val tokenAttributes = authentication.verifyTokenId(tokenId)
                if (tokenAttributes == null) {
                    finish("Bad token id")
                    return@intercept
                }

                call.attributes.put(AuthenticationKey.deviceId, deviceId)
                call.attributes.putAll(tokenAttributes)
            }

            return plugin
        }

        private suspend fun PipelineContext<*, PipelineCall>.finish(message: String) {
            call.respond(HttpStatusCode.Unauthorized, message)
            finish()
        }
    }
}
