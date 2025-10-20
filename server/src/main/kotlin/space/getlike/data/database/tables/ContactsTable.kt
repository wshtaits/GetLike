package space.getlike.data.database.tables

import org.jetbrains.exposed.v1.core.Table

object ContactsTable : Table("contacts") {

    val profileId = varchar("profile_id", 255)
    val contactId = varchar("contact_id", 255)

    override val primaryKey = PrimaryKey(profileId, contactId)
}