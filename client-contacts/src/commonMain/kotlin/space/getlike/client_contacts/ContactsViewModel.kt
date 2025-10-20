package space.getlike.client_contacts

import space.getlike.client_base.presentation.viewmodel.ViewModel
import space.getlike.models.Chat
import space.getlike.client_base.presentation.routes.ChatRoute
import space.getlike.client_base.presentation.routes.ContactRoute
import space.getlike.client_base.presentation.routes.ContactsRoute
import space.getlike.client_base.presentation.routes.SearchContactRoute
import space.getlike.client_base.presentation.routes.InviteRoute
import space.getlike.models.LocalContact
import space.getlike.util_permissions.Permission

class ContactsViewModel(
    bundle: Bundle,
) : ViewModel<ContactsState, ContactsRoute>(
    bundle = bundle,
    analyticsName = "Contacts",
    initialState = { deps, _ ->
        ContactsState(
            chats = deps.chatRepository.chatsFlow.value
                .filter { chat -> chat.contact.isContact }
                .sortedBy { chat -> chat.contact.name },
            localContacts = deps.profileRepository.unregisteredLocalContactsFlow.value
                .sortedBy { unregisteredLocalContact -> unregisteredLocalContact.name },
            isLoadingLocalContacts = false,
            hasContactsPermission = deps.permissionManager.has(Permission.Contacts),
            syncState = deps.syncRepository.syncState,
        )
    },
) {

    init {
        deps.chatRepository.chatsFlow.launchCollect { chats ->
            state = state.copy(
                chats = chats
                    .filter { chat -> chat.contact.isContact }
                    .sortedBy { chat -> chat.contact.name },
            )
        }
        deps.profileRepository.unregisteredLocalContactsFlow.launchCollect { unregisteredLocalContacts ->
            state = state.copy(
                localContacts = unregisteredLocalContacts
                    .sortedBy { unregisteredLocalContact -> unregisteredLocalContact.name },
                isLoadingLocalContacts = false,
            )
        }
        deps.syncRepository.syncStateFlow.launchCollect { syncState ->
            state = state.copy(syncState = syncState)
        }
        deps.permissionManager.observe(Permission.Contacts).launchCollect { isGranted ->
            state = state.copy(hasContactsPermission = isGranted)
        }
    }

    fun onSearchContactActionClick() = event("SearchContact", Action, Click) {
        navigateRoot(SearchContactRoute())
    }

    fun onImportContactsClick() = event("ImportContacts", Button, Click) {
        importContacts()
    }

    fun onImportContactsActionClick() = event("ImportContacts", Action, Click) {
        importContacts()
    }

    fun onContactClick(chat: Chat) = event("Contact", ListItem, Click) {
        navigateRoot(ContactRoute(chat.contact.id))
    }

    fun onContactLikeClick(chat: Chat) = event("Like", Button, Click) {
        navigateRoot(ChatRoute(chat))
    }

    fun onLocalContactInviteClick(localContact: LocalContact) = event("Invite", Button, Click) {
        navigateModal(InviteRoute(localContact))
    }

    private suspend fun importContacts() {
        state = state.copy(
            isLoadingLocalContacts = true,
        )
        val hasPermission = deps.permissionManager.request(Permission.Contacts)
        if (!hasPermission) {
            state = state.copy(
                isLoadingLocalContacts = false,
            )
        }
    }
}