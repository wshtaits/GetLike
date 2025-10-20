package space.getlike.util_messaging

import io.ktor.server.application.ApplicationCall
import io.ktor.server.websocket.WebSocketServerSession

interface MessagingContext {

    fun registerSession(call: ApplicationCall, session: WebSocketServerSession)

    fun unregisterSession(call: ApplicationCall, session: WebSocketServerSession)

    fun getMessageEndpoints(userIds: List<String>): List<MessagingEndpoint>
}