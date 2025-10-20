package space.getlike.client_main

import kotlinx.serialization.Serializable

@Serializable
data class MainState(
    val hasUnreadMessages: Boolean,
    val currentScreen: ChildScreen,
) {

    val isProfileSelected: Boolean = currentScreen == ChildScreen.Profile
    val isChatsSelected: Boolean = currentScreen == ChildScreen.Chats
    val isContactsSelected: Boolean = currentScreen == ChildScreen.Contacts

    enum class ChildScreen {
        Profile,
        Chats,
        Contacts,
    }
}