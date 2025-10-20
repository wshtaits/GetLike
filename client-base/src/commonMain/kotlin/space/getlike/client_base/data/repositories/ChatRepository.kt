package space.getlike.client_base.data.repositories

import kotlinx.coroutines.flow.*
import space.getlike.client_base.dependencies.DataDependencies
import space.getlike.models.Chat
import space.getlike.models.Message
import space.getlike.models.MessageStatus
import space.getlike.util_core.Repository
import space.getlike.util_core.utils.add
import space.getlike.util_core.utils.remove
import space.getlike.util_core.utils.removeAndAdd
import space.getlike.util_core.utils.replaceBy
import kotlin.collections.map
import kotlin.collections.orEmpty

class ChatRepository(
    private val deps: DataDependencies,
) : Repository(deps.coroutineScope) {

    val chatsFlow: StateFlow<List<Chat>> =
        combine(
            deps.contactsFlow,
            deps.messagesFlow,
            transform = { contacts, messages ->
                val groupedMessages = messages
                    .groupBy { message ->
                        if (message.senderId == deps.selfProfileFlow.value?.id) {
                            message.receiverId
                        } else {
                            message.senderId
                        }
                    }
                contacts.map { contact ->
                    Chat(
                        contact = contact,
                        messages = groupedMessages[contact.id].orEmpty(),
                    )
                }
            },
        ).stateIn(emptyList())

    val hasUnreadMessagesFlow: StateFlow<Boolean> =
        chatsFlow
            .map { chats ->
                chats
                    .flatMap { chat -> chat.messages }
                    .filter { message -> message.receiverId == deps.selfProfileFlow.value?.id }
                    .any { message -> message.isUnread }
            }
            .stateIn(false)

    fun getChatFlow(contactId: String): Flow<Chat> =
        chatsFlow
            .mapNotNull { chats -> chats.find { chat -> chat.contact.id == contactId } }
            .distinctUntilChanged()

    suspend fun getChat(contactId: String): Result<Chat?> = executeIoCatching {
        val cachedChat = getChatCached(contactId)
        return@executeIoCatching cachedChat ?: deps.getChatRequest.execute(contactId)
    }

    fun getChatCached(contactId: String): Chat? =
        chatsFlow.value
            .find { chat -> chat.contact.id == contactId }

    suspend fun sendMessage(receiverId: String, likesCount: Int) = executeIo {
        val senderId = deps.selfProfileFlow.value?.id ?: return@executeIo

        val unsentMessage = Message.unsent(
            senderId = senderId,
            receiverId = receiverId,
            likesCount = likesCount,
        )

        deps.messagesFlow.add(unsentMessage)

        try {
            val message = deps.sendMessageRequest.execute(unsentMessage)

            if (message == null) {
                deps.messagesFlow.remove(unsentMessage)
            } else {
                deps.messagesFlow.removeAndAdd(unsentMessage, message)
                deps.messageQueries.upsert(message)
            }
        } catch (_: Exception) {
            deps.messageQueries.upsert(unsentMessage)
        }
    }

    suspend fun addMessagesLocal(messages: List<Message>) = executeIo {
        deps.messagesFlow.add(messages)
        deps.messageQueries.upsertAll(messages)
    }

    suspend fun markRead(contactId: String) = executeIo {
        deps.messagesFlow.replaceBy(
            predicate = { message -> message.senderId == contactId },
            block = { copy(status = MessageStatus.Read) },
        )
        deps.messageQueries.markReadByContactId(contactId)
        try {
            deps.markReadRequest.execute(contactId)
        } catch (_: Exception) {
            deps.messageQueries.upsertDeferredMarkedRead(contactId)
        }
    }

    suspend fun markRead(senderIds: List<String>, receiverId: String) = executeIo {
        deps.messagesFlow.replaceBy(
            predicate = { message -> message.senderId in senderIds && message.receiverId == receiverId },
            block = { copy(status = MessageStatus.Read) }
        )
        deps.messageQueries.markReadBySenderIdAndReceiverId(senderIds, receiverId)
    }
}
