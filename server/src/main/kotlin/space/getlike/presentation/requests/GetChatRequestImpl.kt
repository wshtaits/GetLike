package space.getlike.presentation.requests

import space.getlike.models.Chat
import space.getlike.presentation.requests.base.Request
import space.getlike.requests.GetChatRequest

class GetChatRequestImpl(bundle: Bundle) : Request(bundle), GetChatRequest {

    override suspend fun execute(contactId: String): Chat? =
        deps.chatRepository.getChat(profileId, contactId)
}