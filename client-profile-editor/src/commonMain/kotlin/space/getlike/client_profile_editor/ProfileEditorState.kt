package space.getlike.client_profile_editor

import kotlinx.serialization.Serializable
import space.getlike.models.Profile

@Serializable
data class ProfileEditorState(
    val profile: Profile?,
    val avatarPath: String?,
    val name: String?,
    val isNameValid: Boolean,
    val isLoading: Boolean,
    val hasConnection: Boolean,
    val shouldShowImageMenu: Boolean,
)