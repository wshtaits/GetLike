package space.getlike.client_base.presentation.messaging

import space.getlike.client_base.dependencies.PresentationDependencies
import space.getlike.messaging.ReadMessagesMessaging
import space.getlike.util_messaging.ClientMessagingHandler

class ReadMessagesHandler(
    private val deps: PresentationDependencies,
) : ClientMessagingHandler<ReadMessagesMessaging.Content>(ReadMessagesMessaging) {

    override suspend fun handle(content: ReadMessagesMessaging.Content) {
        deps.chatRepository.markRead(content.senderIds, content.receiverId)
    }
}