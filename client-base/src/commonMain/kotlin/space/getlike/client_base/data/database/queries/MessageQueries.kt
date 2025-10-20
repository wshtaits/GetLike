package space.getlike.client_base.data.database.queries

import space.getlike.client_base.data.database.Database
import space.getlike.client_base.data.database.dbos.DeferredMarkedReadDbo
import space.getlike.client_base.data.database.dbos.MessageDbo
import space.getlike.client_base.data.database.queries.base.Queries
import space.getlike.models.Message
import space.getlike.models.MessageStatus
import space.getlike.util_core.utils.nullIfEmpty
import kotlin.time.Instant

class MessageQueries(database: Database) : Queries(database) {

    suspend fun selectAll(): List<Message> = transaction {
        messageDao()
            .selectAll()
            .map { messageDbo -> messageDbo.fromDbo() }
    }

    suspend fun upsert(message: Message) = transaction {
        messageDao().upsert(message.toDbo())
    }

    suspend fun upsertAll(messages: List<Message>) = transaction {
        messageDao().upsertAll(messages.map { message -> message.toDbo() })
    }

    suspend fun replaceAll(messages: List<Message>) = transaction {
        messageDao().replaceAll(messages.map { message -> message.toDbo() })
    }

    suspend fun markReadByContactId(contactId: String) = transaction {
        val messages = messageDao()
            .selectAllBySenderId(contactId)
            .map { message -> message.copy(status = MessageStatus.Read.stringValue) }
        messageDao().upsertAll(messages)
    }

    suspend fun markReadBySenderIdAndReceiverId(senderIds: List<String>, receiverId: String) = transaction {
        val messages = messageDao()
            .selectAllBySenderIdsAndReceiverId(senderIds, receiverId)
            .map { message -> message.copy(status = MessageStatus.Read.stringValue) }
        messageDao().upsertAll(messages)
    }

    suspend fun selectAllDeferredMarkedRead(): List<String> = transaction {
        deferredMarkedReadDao()
            .selectAll()
            .map { deferredMarkedReadDbo -> deferredMarkedReadDbo.fromDbo() }
    }

    suspend fun upsertDeferredMarkedRead(contactId: String) = transaction {
        deferredMarkedReadDao().upsert(contactId.toDbo())
    }

    suspend fun deleteAllDeferredMarkedRead() = transaction {
        deferredMarkedReadDao().deleteAll()
    }

    suspend fun deleteAll() = transaction {
        messageDao().deleteAll()
        deferredMarkedReadDao().deleteAll()
    }

    private fun MessageDbo.fromDbo(): Message =
        Message(
            clientId = clientId.nullIfEmpty(),
            serverId = serverId.nullIfEmpty(),
            senderId = senderId,
            receiverId = receiverId,
            timestamp = Instant.fromEpochSeconds(timestamp),
            status = MessageStatus.fromString(status),
            likesCount = likesCount,
        )

    private fun Message.toDbo(): MessageDbo =
        MessageDbo(
            clientId = clientId.orEmpty(),
            serverId = serverId.orEmpty(),
            senderId = senderId,
            receiverId = receiverId,
            timestamp = timestamp.epochSeconds,
            status = status.stringValue,
            likesCount = likesCount,
        )

    private fun DeferredMarkedReadDbo.fromDbo(): String =
        this.contactId

    private fun String.toDbo(): DeferredMarkedReadDbo =
        DeferredMarkedReadDbo(
            contactId = this,
        )
}