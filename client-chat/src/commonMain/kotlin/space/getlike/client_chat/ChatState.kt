package space.getlike.client_chat

import kotlinx.serialization.Serializable
import space.getlike.models.Chat

@Serializable
data class ChatState(
    val chat: Chat,
    val isAddingContact: Boolean,
)