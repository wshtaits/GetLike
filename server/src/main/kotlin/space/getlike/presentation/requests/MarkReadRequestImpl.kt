package space.getlike.presentation.requests

import space.getlike.messaging.ReadMessagesMessaging
import space.getlike.presentation.requests.base.Request
import space.getlike.requests.MarkReadRequest

class MarkReadRequestImpl(bundle: Bundle) : Request(bundle), MarkReadRequest {

    override suspend fun execute(contactId: String) {
        deps.chatRepository.markRead(senderId = contactId, receiverId = profileId)

        val envelope = ReadMessagesMessaging(
            senderId = contactId,
            receiverId = profileId,
        )
        deps.messagingSender.send(
            userId = profileId,
            envelope = envelope,
            exceptDeviceId = deviceId,
            isWebSocketOnly = true,
        )
        deps.messagingSender.send(
            userId = contactId,
            envelope = envelope,
            isWebSocketOnly = true,
        )
    }
}