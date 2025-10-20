package space.getlike.requests

import kotlinx.rpc.annotations.Rpc
import kotlinx.serialization.Serializable
import space.getlike.models.LocalContact
import space.getlike.models.Profile

@Rpc
interface MergeContactsRequest {

    suspend fun execute(localContacts: List<LocalContact>): Result

    @Serializable
    data class Result(
        val profile: Profile,
        val contacts: List<Profile>,
        val registeredLocalContactIds: List<String>,
    )
}
