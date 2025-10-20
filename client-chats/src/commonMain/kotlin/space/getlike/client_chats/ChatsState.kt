package space.getlike.client_chats

import kotlinx.serialization.Serializable
import space.getlike.models.Chat
import space.getlike.client_base.data.repositories.SyncRepository

@Serializable
data class ChatsState(
    val chats: List<Chat>,
    val syncState: SyncRepository.SyncState,
)