package space.getlike.client_base.data.database.dbos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementDbo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val profileId: String,
    val isGranted: Boolean,
    val progress: String,
    val emoji: String,
    val color: Long,
    val text: String,
)