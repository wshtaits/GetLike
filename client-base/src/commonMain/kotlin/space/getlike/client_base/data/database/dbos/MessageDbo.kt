package space.getlike.client_base.data.database.dbos

import androidx.room.Entity

@Entity(
    tableName = "messages",
    primaryKeys = ["clientId", "serverId"],
)
data class MessageDbo(
    val clientId: String,
    val serverId: String,
    val senderId: String,
    val receiverId: String,
    val timestamp: Long,
    val status: String,
    val likesCount: Int,
)