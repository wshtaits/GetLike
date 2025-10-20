package space.getlike.util_messaging

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.sendSerialized

class ClientMessagingSender {

    internal lateinit var session: DefaultClientWebSocketSession

    suspend fun send(envelope: MessagingEnvelope) =
        session.sendSerialized(envelope)
}