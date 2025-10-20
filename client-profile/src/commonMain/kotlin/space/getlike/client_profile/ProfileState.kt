package space.getlike.client_profile

import kotlinx.serialization.Serializable
import space.getlike.client_base.data.repositories.SyncRepository
import space.getlike.models.Profile

@Serializable
data class ProfileState(
    val profile: Profile,
    val syncState: SyncRepository.SyncState,
)