package space.getlike.client_base.data.database.dbos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileDbo(
    @PrimaryKey val id: String,
    val avatarUrl: String?,
    val name: String,
    val totalLikesSent: Int,
    val totalLikesReceived: Int,
    val status: String,
)