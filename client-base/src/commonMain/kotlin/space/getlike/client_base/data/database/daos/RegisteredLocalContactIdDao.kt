package space.getlike.client_base.data.database.daos

import androidx.room.*
import space.getlike.client_base.data.database.dbos.RegisteredLocalContactIdDbo

@Dao
interface RegisteredLocalContactIdDao {

    @Query("SELECT * FROM registered_local_contact_ids")
    suspend fun selectAll(): List<RegisteredLocalContactIdDbo>

    @Upsert
    suspend fun upsertAll(dbos: List<RegisteredLocalContactIdDbo>)

    @Query("DELETE FROM registered_local_contact_ids")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(dbos: List<RegisteredLocalContactIdDbo>) {
        deleteAll()
        upsertAll(dbos)
    }
}