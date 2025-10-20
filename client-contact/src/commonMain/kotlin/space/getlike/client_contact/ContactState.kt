package space.getlike.client_contact

import kotlinx.serialization.Serializable
import space.getlike.models.Chat

@Serializable
data class ContactState(
    val chat: Chat?,
    val isAddingContact: Boolean,
)