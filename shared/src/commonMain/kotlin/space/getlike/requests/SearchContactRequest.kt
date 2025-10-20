package space.getlike.requests

import kotlinx.rpc.annotations.Rpc
import kotlinx.serialization.Serializable

@Rpc
interface SearchContactRequest {

    suspend fun execute(phone: String): Result

    @Serializable
    sealed interface Result {

        @Serializable
        object Self : Result

        @Serializable
        object NotRegistered : Result

        @Serializable
        data class Success(val contactId: String) : Result
    }
}