package space.getlike.requests

import kotlinx.rpc.annotations.Rpc
import kotlinx.serialization.Serializable
import space.getlike.models.LocalContact
import space.getlike.models.Message
import space.getlike.models.Profile

@Rpc
interface SyncRequest {

    suspend fun execute(
        pushToken: String?,
        markedReadContactIds: List<String>,
        unsentMessages: List<Message>,
        localContacts: List<LocalContact>,
    ): Result

    @Serializable
    data class Result(
        val profile: Profile,
        val contacts: List<Profile>,
        val messages: List<Message>,
        val registeredLocalContactIds: List<String>,
    )
}