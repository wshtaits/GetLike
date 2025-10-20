package space.getlike.client_base.data.database.dbos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registered_local_contact_ids")
data class RegisteredLocalContactIdDbo(
    @PrimaryKey val registeredLocalContactId: String,
)