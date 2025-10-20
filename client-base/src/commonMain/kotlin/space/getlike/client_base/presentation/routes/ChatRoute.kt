package space.getlike.client_base.presentation.routes

import kotlinx.serialization.Serializable
import space.getlike.models.Chat
import space.getlike.util_core.Route

@Serializable
data class ChatRoute(
    val chat: Chat,
) : Route()