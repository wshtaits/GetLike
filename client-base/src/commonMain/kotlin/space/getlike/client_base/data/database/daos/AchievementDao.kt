package space.getlike.client_base.data.database.daos

import androidx.room.*
import space.getlike.client_base.data.database.dbos.AchievementDbo

@Dao
interface AchievementDao {

    @Query("SELECT * FROM achievements")
    suspend fun selectAll(): List<AchievementDbo>

    @Upsert
    suspend fun upsertAll(dbos: List<AchievementDbo>)

    @Query("DELETE FROM achievements")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(dbos: List<AchievementDbo>) {
        deleteAll()
        upsertAll(dbos)
    }
}