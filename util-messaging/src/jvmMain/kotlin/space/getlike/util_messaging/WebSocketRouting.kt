package space.getlike.util_messaging

import io.ktor.server.routing.Routing
import io.ktor.server.websocket.receiveDeserialized
import io.ktor.server.websocket.webSocket

fun Routing.webSocket(
    path: String,
    context: MessagingContext,
    handlers: List<ServerMessagingHandler<*>>,
) =
    webSocket(path = path) {
        val receiver = MessagingReceiver(handlers)
        context.registerSession(call, this)
        try {
            while (true) {
                receiver.receive(
                    envelope = receiveDeserialized<MessagingEnvelope>(),
                    extra = call,
                )
            }
        } finally {
            context.unregisterSession(call, this)
        }
    }