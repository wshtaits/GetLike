package space.getlike.client_invite

import kotlinx.serialization.Serializable
import space.getlike.client_base.presentation.routes.InviteRoute
import space.getlike.util_share.ShareDestination

@Serializable
data class InviteState(
    val content: InviteRoute.Content,
    val selectedPhone: String,
    val deeplink: String,
    val destinations: List<ShareDestination>,
    val isTextCopied: Boolean,
)