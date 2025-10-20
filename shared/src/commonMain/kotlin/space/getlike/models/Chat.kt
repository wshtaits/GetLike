package space.getlike.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Chat(
    val contact: Profile,
    val messages: List<Message>,
) {

    val sentLikesCount: Int =
        messages
            .filter { message -> message.receiverId == contact.id }
            .sumOf(Message::likesCount)

    val receivedLikesCount: Int =
        messages
            .filter { message -> message.senderId == contact.id }
            .sumOf(Message::likesCount)

    @Contextual
    val latestTimestamp: Instant? =
        messages
            .maxOfOrNull(Message::timestamp)

    companion object {

        const val MAX_LIKES_COUNT: Int = 10000

        fun example(): Chat {
            val exampleProfile = Profile.example()
            val exampleContactProfile = Profile.example()
            val hiMessages = listOf(
                Message.example(
                    senderId = exampleProfile.id,
                    receiverId = exampleContactProfile.id,
                    likesCount = 0,
                ),
                Message.example(
                    senderId = exampleContactProfile.id,
                    receiverId = exampleProfile.id,
                    likesCount = 0,
                ),
            )
            val likeMessages = List(10) { index ->
                val (senderId, receiverId) = if (index % 2 == 0) {
                    exampleContactProfile.id to exampleProfile.id
                } else {
                    exampleProfile.id to exampleContactProfile.id
                }
                Message.example(
                    senderId = senderId,
                    receiverId = receiverId,
                )
            }
            return Chat(
                contact = exampleContactProfile,
                messages = hiMessages + likeMessages,
            )
        }
    }
}