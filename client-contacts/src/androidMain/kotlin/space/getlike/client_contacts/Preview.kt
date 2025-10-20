package space.getlike.client_contacts

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import space.getlike.client_base.data.repositories.SyncRepository
import space.getlike.client_base.presentation.design.ThemedPreview
import space.getlike.models.Chat
import space.getlike.models.LocalContact

@Preview
@Composable
fun Preview() {
    ThemedPreview(
        view = ::ContactsView,
        state = ContactsState(
            chats = List(5) { Chat.example() },
            localContacts = List(5) { LocalContact.example() },
            isLoadingLocalContacts = false,
            hasContactsPermission = false,
            syncState = SyncRepository.SyncState.Loading,
        ),
    )
}