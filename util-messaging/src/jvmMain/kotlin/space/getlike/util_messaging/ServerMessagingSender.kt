package space.getlike.util_messaging

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import io.ktor.server.websocket.sendSerialized
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock

class ServerMessagingSender(
    private val firebaseMessaging: FirebaseMessaging,
    private val context: MessagingContext,
) {

    suspend fun send(
        userId: String,
        envelope: MessagingEnvelope,
        exceptDeviceId: String? = null,
        isWebSocketOnly: Boolean = false,
    ) = send(listOf(userId), envelope, exceptDeviceId, isWebSocketOnly)

    suspend fun send(
        userIds: List<String>,
        envelope: MessagingEnvelope,
        exceptDeviceId: String? = null,
        isWebSocketOnly: Boolean = false,
    ) {
        val endpoints = context.getMessageEndpoints(userIds)
            .filter { (deviceId, _, _) -> deviceId != exceptDeviceId }
        val sessionAndMutexs = endpoints
            .mapNotNull { (_, _, sessionAndMutex) -> sessionAndMutex }
        val pushTokens = endpoints
            .filter { (_, _, session) -> session == null }
            .map { (_, pushToken, _) -> pushToken }

        if (!isWebSocketOnly && pushTokens.isNotEmpty()) {
            firebaseMessaging.sendEachForMulticastAsync(
                MulticastMessage.builder()
                    .apply {
                        pushTokens.forEach(::addToken)
                        putData(MessagingEnvelope.KEY_NAME, envelope.name)
                        putData(MessagingEnvelope.KEY_CONTENT_JSON, envelope.contentJson)
                    }
                    .build()
            )
        }

        coroutineScope {
            sessionAndMutexs.forEach { (session, mutex) ->
                launch {
                    mutex.withLock { session.sendSerialized(envelope) }
                }
            }
        }
    }
}