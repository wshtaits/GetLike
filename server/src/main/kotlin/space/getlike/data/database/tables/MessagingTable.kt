package space.getlike.data.database.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.timestamp

object MessagingTable : Table("messaging") {

    val profileId = varchar("profile_id", length = 255)
    val deviceId = varchar("device_id", length = 255)
    val pushToken = varchar("push_token", length = 255)
    val lastSeen = timestamp("last_seen")

    override val primaryKey = PrimaryKey(profileId, deviceId)
}