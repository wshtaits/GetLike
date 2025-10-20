package space.getlike.data.database.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.javatime.timestamp

object MessagesTable : IntIdTable("messages") {

    val senderId = varchar("sender_id", 255)
    val receiverId = varchar("receiver_id", 255)
    val timestamp = timestamp("timestamp")
    val status = varchar("status", 255)
    val likesCount = integer("likes_count")
}