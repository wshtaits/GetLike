package space.getlike.requests

import kotlinx.rpc.annotations.Rpc

@Rpc
interface MarkReadRequest {

    suspend fun execute(contactId: String)
}