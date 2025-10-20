package space.getlike.client_base.data.database.daos

import androidx.room.*
import space.getlike.client_base.data.database.dbos.MessageDbo

@Dao
interface MessageDao {

    @Query("SELECT * FROM messages")
    suspend fun selectAll(): List<MessageDbo>

    @Query("SELECT * FROM messages where senderId = :senderId")
    suspend fun selectAllBySenderId(senderId: String): List<MessageDbo>

    @Query("SELECT * FROM messages WHERE senderId IN (:senderIds) AND receiverId = :receiverId")
    suspend fun selectAllBySenderIdsAndReceiverId(senderIds: List<String>, receiverId: String): List<MessageDbo>

    @Upsert
    suspend fun upsert(dbo: MessageDbo)

    @Upsert
    suspend fun upsertAll(dbos: List<MessageDbo>)

    @Query("DELETE FROM messages")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(dbos: List<MessageDbo>) {
        deleteAll()
        upsertAll(dbos)
    }
}