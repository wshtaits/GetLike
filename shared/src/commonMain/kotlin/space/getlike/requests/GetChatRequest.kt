package space.getlike.requests

import kotlinx.rpc.annotations.Rpc
import space.getlike.models.Chat

@Rpc
interface GetChatRequest {

    suspend fun execute(contactId: String): Chat?
}