package space.getlike.client_chats

import space.getlike.client_base.presentation.viewmodel.ViewModel
import space.getlike.models.Chat
import space.getlike.client_base.presentation.routes.ChatRoute
import space.getlike.client_base.presentation.routes.ChatsRoute

class ChatsViewModel(
    bundle: Bundle,
) : ViewModel<ChatsState, ChatsRoute>(
    bundle = bundle,
    analyticsName = "Chats",
    initialState = { deps, _ ->
        ChatsState(
            chats = deps.chatRepository.chatsFlow.value,
            syncState = deps.syncRepository.syncState,
        )
    },
) {

    init {
        deps.chatRepository.chatsFlow.launchCollect { chats ->
            state = state.copy(chats = chats)
        }
        deps.syncRepository.syncStateFlow.launchCollect { syncState ->
            state = state.copy(syncState = syncState)
        }
    }

    fun onChatClick(chat: Chat) = event("Chat", ListItem, Click) {
        navigateRoot(ChatRoute(chat))
    }
}