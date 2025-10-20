package space.getlike.client_base.data.database.dbos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deferred_marked_read")
data class DeferredMarkedReadDbo(
    @PrimaryKey val contactId: String,
)