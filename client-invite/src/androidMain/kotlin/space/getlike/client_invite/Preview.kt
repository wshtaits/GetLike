package space.getlike.client_invite

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import space.getlike.client_base.presentation.design.ThemedPreview
import space.getlike.client_base.presentation.routes.InviteRoute
import space.getlike.models.LocalContact
import space.getlike.util_core.Example
import space.getlike.util_share.ShareDestination

@Preview
@Composable
fun Preview() {
    ThemedPreview(
        view = ::InviteView,
        state = {
            val contact = LocalContact.example()
            InviteState(
                content = InviteRoute.Content.Contact(contact),
                deeplink = Example.string(100),
                selectedPhone = contact.phones.first(),
                destinations = ShareDestination.entries,
                isTextCopied = false,
            )
        },
    )
}