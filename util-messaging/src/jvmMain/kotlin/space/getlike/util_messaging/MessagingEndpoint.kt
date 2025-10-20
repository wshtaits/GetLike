package space.getlike.util_messaging

import io.ktor.server.websocket.WebSocketServerSession
import kotlinx.coroutines.sync.Mutex

data class MessagingEndpoint(
    val deviceId: String,
    val pushToken: String,
    val sessionAndMutex: Pair<WebSocketServerSession, Mutex>?,
)