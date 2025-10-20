package space.getlike.messaging

import kotlinx.serialization.Serializable
import space.getlike.util_messaging.Messaging

object ReadMessagesMessaging : Messaging<ReadMessagesMessaging.Content>(
    name = "ReadMessages",
    serializer = Content.serializer(),
) {

    operator fun invoke(senderIds: List<String>, receiverId: String) =
        invoke(Content(senderIds, receiverId))

    operator fun invoke(senderId: String, receiverId: String) =
        invoke(Content(listOf(senderId), receiverId))

    @Serializable
    data class Content(
        val senderIds: List<String>,
        val receiverId: String,
    )
}