package space.getlike.util_messaging

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.webSocket

suspend fun HttpClient.webSocket(
    url: String,
    sender: ClientMessagingSender,
    handlers: List<ClientMessagingHandler<*>>,
) =
    try {
        webSocket(url) {
            sender.session = this
            val receiver = MessagingReceiver(handlers)
            while (true) {
                receiver.receive(
                    envelope = receiveDeserialized<MessagingEnvelope>(),
                    extra = Unit,
                )
            }
        }
    } catch (_: Exception) {
        // no op
    }
