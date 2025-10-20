package space.getlike.client_base.data.database.daos

import androidx.room.*
import space.getlike.client_base.data.database.dbos.ProfileDbo

@Dao
interface ProfileDao {

    @Query("SELECT * FROM profiles")
    suspend fun selectAll(): List<ProfileDbo>

    @Query("SELECT * FROM profiles WHERE id = :profileId")
    suspend fun selectByProfileId(profileId: String): ProfileDbo?

    @Upsert
    suspend fun upsert(dbo: ProfileDbo)

    @Upsert
    suspend fun upsertAll(dbos: List<ProfileDbo>)

    @Query("DELETE FROM profiles")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(dbos: List<ProfileDbo>) {
        deleteAll()
        upsertAll(dbos)
    }
}