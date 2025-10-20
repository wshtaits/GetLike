package space.getlike.data.database.tables

import org.jetbrains.exposed.v1.core.Table

object ProfilesTable : Table("profiles") {

    val id = varchar("id", 255)
    val avatarUrl = varchar("avatar_url", 255).nullable()
    val name = varchar("name", 255)
    val totalLikesSent = integer("total_likes_sent")
    val totalLikesReceived = integer("total_likes_received")

    override val primaryKey = PrimaryKey(id)
}