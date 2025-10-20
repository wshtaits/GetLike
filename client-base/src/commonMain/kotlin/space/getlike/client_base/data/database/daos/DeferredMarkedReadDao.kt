package space.getlike.client_base.data.database.daos

import androidx.room.*
import space.getlike.client_base.data.database.dbos.DeferredMarkedReadDbo

@Dao
interface DeferredMarkedReadDao {

    @Query("SELECT * FROM deferred_marked_read")
    suspend fun selectAll(): List<DeferredMarkedReadDbo>

    @Upsert
    suspend fun upsert(dbo: DeferredMarkedReadDbo)

    @Query("DELETE FROM deferred_marked_read")
    suspend fun deleteAll()
}