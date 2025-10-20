package space.getlike.data.repositories

import space.getlike.dependencies.DataDependencies
import space.getlike.models.Chat
import space.getlike.models.Message
import space.getlike.models.MessageStatus
import space.getlike.models.ProfileStatus
import space.getlike.util_core.utils.clockNow
import kotlin.time.Instant

class ChatRepository(
    private val deps: DataDependencies,
) {

    fun getChat(profileId: String, contactId: String): Chat? {
        val contactContent = deps.profilesQueries.selectProfileById(contactId) ?: return null

        val contact = contactContent.toProfile(
            status = when {
                profileId == contactId -> ProfileStatus.Self
                deps.contactsQueries.contains(profileId, contactId) -> ProfileStatus.Contact
                else -> ProfileStatus.NotContact
            },
            achievements = deps.achievementsProvider.getAchievementsByProfileId(contactId),
        )

        return Chat(
            contact = contact,
            messages = deps.messagesQueries.selectAllByProfileIdAndContactId(profileId, contactId),
        )
    }

    fun getMessagesByProfileId(profileId: String): List<Message> =
        deps.messagesQueries.selectAllByProfileId(profileId)

    fun markRead(senderId: String, receiverId: String) =
        deps.messagesQueries.markRead(listOf(senderId), receiverId)

    fun markRead(senderIds: List<String>, receiverId: String) =
        deps.messagesQueries.markRead(senderIds, receiverId)

    fun addMessage(message: Message): Message? {
        val likesSent = deps.messagesQueries.selectAllBySenderIdAndReceiverId(message.senderId, message.receiverId)
            .sumOf { message -> message.likesCount }

        val adjustedMessage = message.copy(
            timestamp = Instant.clockNow(),
            status = MessageStatus.Unread,
            likesCount = when {
                likesSent == Chat.MAX_LIKES_COUNT -> return null
                likesSent + message.likesCount > Chat.MAX_LIKES_COUNT -> Chat.MAX_LIKES_COUNT - likesSent
                else -> message.likesCount
            },
        )

        deps.profilesQueries.updateLikes(
            mapOf(
                adjustedMessage.senderId to (adjustedMessage.likesCount to 0),
                adjustedMessage.receiverId to (0 to adjustedMessage.likesCount),
            )
        )
        return deps.messagesQueries.upsertAllAndGetWithServerId(listOf(adjustedMessage)).first()
    }

    fun addMessages(unsentMessages: List<Message>): List<Message> {
        val senderIdAndReceiverIdToUnsentMessagesMap = unsentMessages
            .groupBy { message -> message.senderId to message.receiverId }

        val unreadMessages = deps.messagesQueries
            .selectAllBySenderIdAndReceiverIds(senderIdAndReceiverIdToUnsentMessagesMap.keys)
            .mapValues { (_, messages) -> messages.sumOf { message -> message.likesCount } }
            .flatMap { (senderIdAndReceiverId, likesSent) ->
                var currentLikesSent = likesSent
                senderIdAndReceiverIdToUnsentMessagesMap[senderIdAndReceiverId]
                    ?.mapNotNull { message ->
                        val adjustedMessage = message.copy(
                            timestamp = Instant.clockNow(),
                            status = MessageStatus.Unread,
                            likesCount = when {
                                likesSent == Chat.MAX_LIKES_COUNT ->
                                    return@mapNotNull null
                                likesSent + message.likesCount > Chat.MAX_LIKES_COUNT ->
                                    Chat.MAX_LIKES_COUNT - likesSent
                                else ->
                                    message.likesCount
                            },
                        )

                        currentLikesSent += message.likesCount

                        return@mapNotNull adjustedMessage
                    }
                    .orEmpty()
            }

        val messages = deps.messagesQueries.upsertAllAndGetWithServerId(unreadMessages)

        val senderIdToLikesCountMap = messages
            .groupBy { message -> message.senderId }
            .mapValues { (_, messages) -> messages.sumOf(Message::likesCount) to 0 }
        val receiverIdToLikesCountMap = messages
            .groupBy { message -> message.receiverId }
            .mapValues { (_, messages) -> 0 to messages.sumOf(Message::likesCount) }

        deps.profilesQueries.updateLikes(senderIdToLikesCountMap + receiverIdToLikesCountMap)

        return messages
    }
}