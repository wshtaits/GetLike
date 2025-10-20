package space.getlike.client_invite

import org.jetbrains.compose.resources.getString
import space.getlike.client_base.presentation.effects.CopyToClipboardEffect
import space.getlike.client_base.presentation.routes.InviteRoute
import space.getlike.client_base.presentation.viewmodel.ViewModel
import space.getlike.deeplinks.ProfileDeeplink
import space.getlike.resources.*
import space.getlike.util_share.ShareDestination

class InviteViewModel(
    bundle: Bundle,
) : ViewModel<InviteState, InviteRoute>(
    bundle = bundle,
    analyticsName = "Invite",
    initialState = { deps, route ->
        InviteState(
            content = route.content,
            selectedPhone = when (route.content) {
                is InviteRoute.Content.Contact ->
                    (route.content as InviteRoute.Content.Contact).value.phones.firstOrNull().orEmpty()
                is InviteRoute.Content.Phone ->
                    (route.content as InviteRoute.Content.Phone).value
            },
            deeplink = ProfileDeeplink(deps.profileRepository.self?.id.orEmpty()),
            destinations = deps.share.availableDestinations(),
            isTextCopied = false,
        )
    },
) {

    fun onPhoneClick(phone: String) = event("Phone", ListItem, Click) {
        state = state.copy(
            selectedPhone = phone,
        )
    }

    fun onCopyTextClick() = event("CopyText", Button, Click) {
        perform(CopyToClipboardEffect(state.deeplink))
        state = state.copy(
            isTextCopied = true,
        )
    }

    fun onSocialClick(destination: ShareDestination) = event(
        "Social",
        ListItem,
        Click,
        "destination" to destination.name,
    ) {
        deps.share.text(
            text = "${getString(Res.string.invite_message)}\n${state.deeplink}",
            phone = state.selectedPhone,
            destination = destination,
        )
        navigateBack()
    }
}