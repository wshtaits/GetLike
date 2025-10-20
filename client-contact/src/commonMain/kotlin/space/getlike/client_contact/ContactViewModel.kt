package space.getlike.client_contact

import space.getlike.client_base.presentation.routes.ChatRoute
import space.getlike.client_base.presentation.routes.ContactRoute
import space.getlike.client_base.presentation.viewmodel.ViewModel
import space.getlike.deeplinks.ProfileDeeplink

class ContactViewModel(
    bundle: Bundle,
) : ViewModel<ContactState, ContactRoute>(
    bundle = bundle,
    analyticsName = "Contact",
    initialState = { deps, route ->
        ContactState(
            chat = deps.chatRepository.getChatCached(route.contactId),
            isAddingContact = false,
        )
    },
) {

    override fun onLaunch() = eventOnLaunch {
        if (state.chat == null) {
            state = state.copy(
                chat = deps.chatRepository.getChat(route.contactId).getOrNull(),
            )
        }

        deps.chatRepository.getChatFlow(route.contactId).launchCollect { chat ->
            state = state.copy(
                chat = chat,
            )
        }
    }

    fun onBackClick() = event("Back", Button, Click) {
        navigateBack()
    }

    fun onAddContactClick() = event("AddContact", Button, Click) {
        val contactId = state.chat?.contact?.id ?: return@event
        state = state.copy(isAddingContact = true)
        deps.profileRepository.addContact(contactId)
        state = state.copy(isAddingContact = false)
    }

    fun onChatClick() = event("Chat", Button, Click) {
        navigateRoot(ChatRoute(state.chat ?: return@event))
    }

    fun onShareClick() = event("Share", Button, Click) {
        val contactId = state.chat?.contact?.id ?: return@event
        deps.share.text(ProfileDeeplink(contactId))
    }
}