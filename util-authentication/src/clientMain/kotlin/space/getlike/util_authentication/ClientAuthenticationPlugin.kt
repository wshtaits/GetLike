package space.getlike.util_authentication

import io.ktor.client.*
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.*
import io.ktor.util.AttributeKey

class ClientAuthenticationPlugin private constructor(
    private val authentication: Authentication?,
) {

    class Config(
        var authentication: Authentication? = null,
    )

    companion object Plugin : HttpClientPlugin<Config, ClientAuthenticationPlugin> {

        override val key = AttributeKey<ClientAuthenticationPlugin>("AuthenticationPlugin")

        override fun prepare(block: Config.() -> Unit): ClientAuthenticationPlugin {
            val config = Config().apply(block)
            return ClientAuthenticationPlugin(config.authentication)
        }

        override fun install(plugin: ClientAuthenticationPlugin, scope: HttpClient) {
            scope.sendPipeline.intercept(HttpSendPipeline.Monitoring) {
                val tokenId = plugin.authentication?.getTokenId()
                if (!tokenId.isNullOrEmpty()) {
                    context.headers.append(Header.TOKEN_ID, tokenId)
                }

                val deviceId = plugin.authentication?.getDeviceId()
                if (!deviceId.isNullOrEmpty()) {
                    context.headers.append(Header.DEVICE_ID, deviceId)
                }

                proceed()
            }
        }
    }
}
