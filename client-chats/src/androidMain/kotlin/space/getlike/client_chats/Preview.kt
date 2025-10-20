package space.getlike.client_chats

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import space.getlike.client_base.data.repositories.SyncRepository
import space.getlike.client_base.presentation.design.ThemedPreview
import space.getlike.models.Chat

@Preview
@Composable
fun Preview() {
    ThemedPreview(
        view = ::ChatsView,
        state = ChatsState(
            chats = List(10) { Chat.example() },
            syncState = SyncRepository.SyncState.Loading,
        ),
    )
}