package space.getlike.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import space.getlike.util_core.Example
import space.getlike.util_core.utils.clockNow
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class Message(
    val clientId: String?,
    val serverId: String?,
    val senderId: String,
    val receiverId: String,
    val status: MessageStatus,
    @Contextual val timestamp: Instant,
    val likesCount: Int,
) {

    val id: String =
        clientId ?: serverId ?: error("Both clientId and serverId are null")

    val isUnsent: Boolean =
        status.isUnsent

    val isUnread: Boolean =
        status.isUnread

    val isRead: Boolean =
        status.isRead

    companion object {

        @OptIn(ExperimentalUuidApi::class)
        fun unsent(
            senderId: String,
            receiverId: String,
            likesCount: Int,
        ): Message =
            Message(
                clientId = Uuid.random().toString(),
                serverId = null,
                senderId = senderId,
                receiverId = receiverId,
                status = MessageStatus.Unsent,
                timestamp = Instant.clockNow(),
                likesCount = likesCount,
            )

        fun example(
            senderId: String = Example.string(),
            receiverId: String = Example.string(),
            likesCount: Int = Example.int(range = 10..1000),
        ): Message =
            Message(
                clientId = Example.string(),
                serverId = Example.string(),
                senderId = senderId,
                receiverId = receiverId,
                status = Example.enum(),
                timestamp = Example.timestampMinus(daysRange = 3..7),
                likesCount = likesCount,
            )
    }
}