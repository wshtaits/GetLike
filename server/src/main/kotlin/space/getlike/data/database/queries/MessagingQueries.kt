package space.getlike.data.database.queries

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.upsert
import space.getlike.data.database.tables.MessagingTable
import space.getlike.presentation.messaging.MessagingItem
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant

class MessagingQueries {

    fun selectAllByProfileIds(profileIds: List<String>): List<MessagingItem> = transaction {
        MessagingTable
            .selectAll()
            .where { MessagingTable.profileId inList profileIds }
            .map { row -> row.fromRow() }
    }

    fun upsert(item: MessagingItem): Unit = transaction {
        MessagingTable
            .upsert { statement ->
                statement[MessagingTable.profileId] = item.profileId
                statement[MessagingTable.deviceId] = item.deviceId
                statement[MessagingTable.pushToken] = item.pushToken
                statement[MessagingTable.lastSeen] = item.lastSeen.toJavaInstant()
            }
    }

    fun upsertLastSeen(profileId: String, deviceId: String, lastSeen: Instant): Unit = transaction {
        MessagingTable
            .upsert { statement ->
                statement[MessagingTable.profileId] = profileId
                statement[MessagingTable.deviceId] = deviceId
                statement[MessagingTable.lastSeen] = lastSeen.toJavaInstant()
            }
    }

    private fun ResultRow.fromRow(): MessagingItem =
        MessagingItem(
            profileId = this[MessagingTable.profileId],
            deviceId = this[MessagingTable.deviceId],
            pushToken = this[MessagingTable.pushToken],
            lastSeen = this[MessagingTable.lastSeen].toKotlinInstant(),
        )
}