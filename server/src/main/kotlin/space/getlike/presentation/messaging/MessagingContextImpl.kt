package space.getlike.presentation.messaging

import io.ktor.server.application.ApplicationCall
import io.ktor.server.websocket.WebSocketServerSession
import kotlinx.coroutines.sync.Mutex
import space.getlike.data.repositories.MessagingRepository
import space.getlike.util_authentication.AuthenticationKey
import space.getlike.util_messaging.MessagingEndpoint
import space.getlike.util_messaging.MessagingContext
import java.util.concurrent.ConcurrentHashMap

class MessagingContextImpl(
    private val messagingRepository: MessagingRepository,
) : MessagingContext {

    private val sessions = ConcurrentHashMap<Pair<String, String>, Pair<WebSocketServerSession, Mutex>>()

    override fun registerSession(call: ApplicationCall, session: WebSocketServerSession) {
        val profileId = call.attributes[AuthenticationKey.profileId]
        val deviceId = call.attributes[AuthenticationKey.deviceId]
        sessions[profileId to deviceId] = session to Mutex()
        messagingRepository.refreshLastSeen(profileId, deviceId)
    }

    override fun unregisterSession(call: ApplicationCall, session: WebSocketServerSession) {
        val profileId = call.attributes[AuthenticationKey.profileId]
        val deviceId = call.attributes[AuthenticationKey.deviceId]
        sessions.remove(profileId to deviceId)
    }

    override fun getMessageEndpoints(userIds: List<String>): List<MessagingEndpoint> =
        messagingRepository.getItemsByProfileIds(userIds)
            .map { item ->
                MessagingEndpoint(
                    deviceId = item.deviceId,
                    pushToken = item.pushToken,
                    sessionAndMutex = sessions[item.profileId to item.deviceId],
                )
            }
}