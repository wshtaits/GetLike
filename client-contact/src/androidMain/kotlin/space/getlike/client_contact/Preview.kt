package space.getlike.client_contact

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import space.getlike.client_base.presentation.design.ThemedPreview
import space.getlike.models.Chat

@Preview
@Composable
fun Preview() {
    ThemedPreview(
        view = ::ContactView,
        state = ContactState(
            chat = Chat.example(),
            isAddingContact = false,
        ),
    )
}