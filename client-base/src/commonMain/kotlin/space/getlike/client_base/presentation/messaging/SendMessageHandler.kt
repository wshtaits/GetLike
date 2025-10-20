package space.getlike.client_base.presentation.messaging

import space.getlike.client_base.dependencies.PresentationDependencies
import space.getlike.deeplinks.ChatDeeplink
import space.getlike.messaging.SendMessagesMessaging
import space.getlike.models.Message
import space.getlike.util_messaging.ClientMessagingHandler
import space.getlike.util_notifications.NotificationImage

class SendMessageHandler(
    private val deps: PresentationDependencies,
) : ClientMessagingHandler<SendMessagesMessaging.Content>(SendMessagesMessaging) {

    override suspend fun handle(content: SendMessagesMessaging.Content) {
        deps.syncRepository.awaitInit()

        deps.chatRepository.addMessagesLocal(content.messages)

        content.messages
            .filter { message -> message.senderId != deps.profileRepository.self?.id }
            .groupBy { message -> message.senderId }
            .mapValues { (_, message) -> message.sumOf(Message::likesCount) }
            .forEach { (senderId, likesCount) ->
                val contact = deps.chatRepository
                    .getChat(senderId)
                    .getOrNull()
                    ?.contact
                    ?: return

                val avatarUri = contact.avatar.uri
                val emojiImage = NotificationImage.Emoji(contact.avatar.fallbackEmoji)
                val image = if (avatarUri != null) {
                    NotificationImage.Uri(avatarUri)
                } else {
                    emojiImage
                }

                deps.notificationDisplayer.showNotification(
                    id = senderId.hashCode(),
                    title = contact.name,
                    body = likesCount.toString(),
                    smallImage = emojiImage,
                    largeImage = image,
                    deeplink = ChatDeeplink(contact.id),
                )
            }
    }
}
