package space.getlike.messaging

import kotlinx.serialization.Serializable
import space.getlike.util_messaging.Messaging

object SendMessagesMessaging : Messaging<SendMessagesMessaging.Content>(
    name = "SendMessages",
    serializer = Content.serializer(),
) {

    operator fun invoke(messages: List<space.getlike.models.Message>) =
        invoke(Content(messages))

    operator fun invoke(message: space.getlike.models.Message) =
        invoke(Content(listOf(message)))

    @Serializable
    data class Content(
        val messages: List<space.getlike.models.Message>,
    )
}