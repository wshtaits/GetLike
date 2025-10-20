package space.getlike.data.database.queries

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import space.getlike.data.database.tables.MessagesTable
import space.getlike.models.Message
import space.getlike.models.MessageStatus
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant

class MessagesQueries {

    fun selectAllByProfileId(profileId: String): List<Message> = transaction {
        MessagesTable
            .selectAll()
            .where { (MessagesTable.senderId eq profileId) or (MessagesTable.receiverId eq profileId) }
            .mapNotNull { row -> row.fromRow() }
    }

    fun selectAllByProfileIdAndContactId(profileId: String, contactId: String): List<Message> = transaction {
        MessagesTable
            .selectAll()
            .where {
                ((MessagesTable.senderId eq profileId) and (MessagesTable.receiverId eq contactId)) or
                        ((MessagesTable.senderId eq contactId) and (MessagesTable.receiverId eq profileId))
            }
            .mapNotNull { row -> row.fromRow() }
    }

    fun selectAllBySenderIdAndReceiverId(senderId: String, receiverId: String): List<Message> = transaction {
        MessagesTable
            .selectAll()
            .where { (MessagesTable.senderId eq senderId) and (MessagesTable.receiverId eq receiverId) }
            .mapNotNull { row -> row.fromRow() }
    }

    fun selectAllBySenderIdAndReceiverIds(
        senderIdAndReceiverIds: Set<Pair<String, String>>
    ): Map<Pair<String, String>, List<Message>> = transaction {
        if (senderIdAndReceiverIds.isEmpty()) {
            return@transaction emptyMap()
        }

        val fullCondition = senderIdAndReceiverIds
            .map { (senderId, receiverId) ->
                (MessagesTable.senderId eq senderId) and (MessagesTable.receiverId eq receiverId)
            }
            .reduce { fullCondition, condition -> fullCondition or condition }

        MessagesTable
            .select(fullCondition)
            .mapNotNull { row -> row.fromRow() }
            .groupBy { message -> message.senderId to message.receiverId }
    }

    fun upsertAllAndGetWithServerId(messages: List<Message>): List<Message> = transaction {
        messages.map { message ->
            val insertedId = MessagesTable.insertAndGetId { statement ->
                statement[senderId] = message.senderId
                statement[receiverId] = message.receiverId
                statement[timestamp] = message.timestamp.toJavaInstant()
                statement[status] = message.status.stringValue
                statement[likesCount] = message.likesCount
            }
            message.copy(serverId = insertedId.value.toString())
        }
    }

    fun markRead(senderIds: List<String>, receiverId: String): Unit = transaction {
        MessagesTable
            .update(
                where = { (MessagesTable.receiverId eq receiverId) and (MessagesTable.senderId inList senderIds) },
            ) { statement ->
                statement[status] = MessageStatus.Read.stringValue
            }
    }

    private fun ResultRow.fromRow(
        clientId: String? = null,
    ): Message =
        Message(
            clientId = clientId,
            serverId = this[MessagesTable.id].toString(),
            senderId = this[MessagesTable.senderId],
            receiverId = this[MessagesTable.receiverId],
            timestamp = this[MessagesTable.timestamp].toKotlinInstant(),
            status = MessageStatus.fromString(this[MessagesTable.status]),
            likesCount = this[MessagesTable.likesCount],
        )
}