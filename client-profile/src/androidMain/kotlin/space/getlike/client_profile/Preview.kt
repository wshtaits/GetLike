package space.getlike.client_profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import space.getlike.client_base.data.repositories.SyncRepository
import space.getlike.client_base.presentation.design.ThemedPreview
import space.getlike.models.Profile

@Preview
@Composable
fun Preview() {
    ThemedPreview(
        view = ::ProfileView,
        state = ProfileState(
            profile = Profile.example(),
            syncState = SyncRepository.SyncState.Loading,
        ),
    )
}