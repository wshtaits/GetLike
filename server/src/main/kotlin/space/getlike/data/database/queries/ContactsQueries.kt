package space.getlike.data.database.queries

import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.batchUpsert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.upsert
import space.getlike.data.database.tables.ContactsTable

class ContactsQueries {

    fun selectContactIds(profileId: String): List<String> = transaction {
        ContactsTable
            .selectAll()
            .where { ContactsTable.profileId eq profileId }
            .mapNotNull { row -> row[ContactsTable.contactId] }
    }

    fun selectBeingContactIds(profileId: String): List<String> = transaction {
        ContactsTable
            .selectAll()
            .where { ContactsTable.contactId eq profileId }
            .mapNotNull { row -> row[ContactsTable.profileId] }
    }

    fun selectContactIdsAndBeingContactIds(profileId: String): List<String> = transaction {
        ContactsTable
            .selectAll()
            .where { (ContactsTable.profileId eq profileId) or (ContactsTable.contactId eq profileId) }
            .mapNotNull { row -> row[ContactsTable.profileId] }
    }

    fun contains(profileId: String, contactId: String): Boolean = transaction {
        ContactsTable
            .selectAll()
            .where { (ContactsTable.profileId eq profileId) and (ContactsTable.contactId eq contactId) }
            .empty()
            .not()
    }

    fun upsert(profileId: String, contactId: String): Unit = transaction {
        ContactsTable
            .upsert { statement ->
                statement[ContactsTable.profileId] = profileId
                statement[ContactsTable.contactId] = contactId
            }
    }

    fun upsert(profileId: String, contactIds: Collection<String>): Unit = transaction {
        ContactsTable
            .batchUpsert(contactIds) { contactId ->
                this[ContactsTable.profileId] = profileId
                this[ContactsTable.contactId] = contactId
            }
    }
}