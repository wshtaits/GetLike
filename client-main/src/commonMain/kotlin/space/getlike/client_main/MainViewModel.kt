package space.getlike.client_main

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import space.getlike.client_base.presentation.broadcasts.ShowProfileBroadcast
import space.getlike.client_base.presentation.effects.ShowSnackbarEffect
import space.getlike.client_base.presentation.viewmodel.ViewModel
import space.getlike.client_base.presentation.routes.ChatRoute
import space.getlike.client_base.presentation.routes.ChatsRoute
import space.getlike.client_base.presentation.routes.ContactRoute
import space.getlike.client_base.presentation.routes.ContactsRoute
import space.getlike.client_base.presentation.routes.MainRoute
import space.getlike.client_base.presentation.routes.ProfileRoute
import space.getlike.client_base.presentation.routes.SearchContactRoute
import space.getlike.deeplinks.ChatDeeplink
import space.getlike.deeplinks.ProfileDeeplink
import space.getlike.deeplinks.SearchContactDeeplink
import space.getlike.resources.*
import space.getlike.util_core.utils.isNotNullAndActive
import space.getlike.util_deeplinks.DeeplinkHandler

class MainViewModel(
    bundle: Bundle,
) : ViewModel<MainState, MainRoute>(
    bundle = bundle,
    analyticsName = "Main",
    initialState = { deps, _ ->
        MainState(
            currentScreen = MainState.ChildScreen.Profile,
            hasUnreadMessages = deps.chatRepository.hasUnreadMessagesFlow.value,
        )
    },
) {

    private var appCloseTimerJob: Job? = null

    init {
        DeeplinkHandler.flow.launchCollect { deeplink ->
            deeplink matches ProfileDeeplink { profileId ->
                navigateRoot(ContactRoute(profileId))
            }
            deeplink matches ChatDeeplink { profileId ->
                val chat = deps.chatRepository.getChatCached(profileId) ?: return@ChatDeeplink
                navigateRoot(ChatRoute(chat))
            }
            deeplink matches SearchContactDeeplink {
                navigateRoot(SearchContactRoute())
            }
        }

        deps.broadcast.receive<ShowProfileBroadcast>().launchCollect {
            state = state.copy(
                currentScreen = MainState.ChildScreen.Profile,
            )
            navigateChild(ProfileRoute(), singleTop = true)
        }

        deps.chatRepository.hasUnreadMessagesFlow.launchCollect { hasUnreadMessages ->
            state = state.copy(
                hasUnreadMessages = hasUnreadMessages,
            )
        }
    }

    override fun onSystemBackClick() = eventOnSystemBack {
        if (appCloseTimerJob.isNotNullAndActive) {
            deps.appLifecycle.close()
        } else {
            perform(ShowSnackbarEffect(Res.string.main_message_exit))
            appCloseTimerJob = launch {
                delay(5_000)
                appCloseTimerJob = null
            }
        }
    }

    fun onProfileClick() = event("Profile", Button, Click) {
        if (!state.isProfileSelected) {
            state = state.copy(
                currentScreen = MainState.ChildScreen.Profile,
            )
            navigateChild(ProfileRoute(), singleTop = true)
        }
    }

    fun onChatsClick() = event("Chats", Button, Click) {
        if (!state.isChatsSelected) {
            state = state.copy(
                currentScreen = MainState.ChildScreen.Chats,
            )
            navigateChild(ChatsRoute(), singleTop = true)
        }
    }

    fun onContactsClick() = event("Contacts", Button, Click) {
        if (!state.isContactsSelected) {
            state = state.copy(
                currentScreen = MainState.ChildScreen.Contacts,
            )
            navigateChild(ContactsRoute(), singleTop = true)
        }
    }
}