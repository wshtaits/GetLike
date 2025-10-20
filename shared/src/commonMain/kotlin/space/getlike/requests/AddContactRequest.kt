package space.getlike.requests

import kotlinx.rpc.annotations.Rpc
import space.getlike.models.Profile

@Rpc
interface AddContactRequest {

    suspend fun execute(contactId: String): Profile?
}