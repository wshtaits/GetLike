package space.getlike.requests

import kotlinx.rpc.annotations.Rpc
import space.getlike.models.Message

@Rpc
interface SendMessageRequest {

    suspend fun execute(message: Message): Message?
}