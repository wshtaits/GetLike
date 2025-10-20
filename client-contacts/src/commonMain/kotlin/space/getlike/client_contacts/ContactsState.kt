package space.getlike.client_contacts

import kotlinx.serialization.Serializable
import space.getlike.models.Chat
import space.getlike.models.LocalContact
import space.getlike.client_base.data.repositories.SyncRepository

@Serializable
data class ContactsState(
    val chats: List<Chat>,
    val localContacts: List<LocalContact>,
    val isLoadingLocalContacts: Boolean,
    val hasContactsPermission: Boolean,
    val syncState: SyncRepository.SyncState,
)