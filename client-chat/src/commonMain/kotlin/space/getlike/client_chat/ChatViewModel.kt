package space.getlike.client_chat

import space.getlike.client_base.presentation.effects.ScrollLazyListEffect
import space.getlike.client_base.presentation.routes.ChatRoute
import space.getlike.client_base.presentation.viewmodel.ViewModel
import space.getlike.client_base.presentation.routes.ContactRoute

class ChatViewModel(
    bundle: Bundle,
) : ViewModel<ChatState, ChatRoute>(
    bundle = bundle,
    analyticsName = "Chat",
    initialState = { _, route ->
        ChatState(
            chat = route.chat,
            isAddingContact = false,
        )
    },
) {

    init {
        deps.chatRepository.getChatFlow(state.chat.contact.id).launchCollect { chat ->
            state = state.copy(chat = chat)
        }
    }

    override fun onLaunch() = eventOnLaunch {
        deps.chatRepository.markRead(state.chat.contact.id)
    }

    fun onBackClick() = event("Back", Button, Click) {
        navigateBack()
    }

    fun onAvatarClick() = event("Avatar", Image, Click) {
        navigateRoot(ContactRoute(state.chat.contact.id))
    }

    fun onAddContactClick() = event("AddContact", Button, Click) {
        state = state.copy(isAddingContact = true)
        deps.profileRepository.addContact(state.chat.contact.id)
        state = state.copy(isAddingContact = false)
    }

    fun onSendLikes(likesCount: Int) = event("Likes", Button, Confirm) {
        deps.chatRepository.sendMessage(
            receiverId = state.chat.contact.id,
            likesCount = likesCount,
        )
        perform(ScrollLazyListEffect())
    }
}