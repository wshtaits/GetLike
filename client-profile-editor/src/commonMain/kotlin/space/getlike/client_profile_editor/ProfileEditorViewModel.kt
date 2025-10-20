package space.getlike.client_profile_editor

import kotlinx.coroutines.Job
import space.getlike.client_base.presentation.effects.HideKeyboardEffect
import space.getlike.client_base.presentation.routes.ProfileEditorRoute
import space.getlike.client_base.presentation.viewmodel.ViewModel
import space.getlike.util_core.utils.cancelAndIfNotFinished
import space.getlike.util_core.utils.isNullOrCompleted
import space.getlike.util_permissions.Permission

class ProfileEditorViewModel(
    bundle: Bundle,
) : ViewModel<ProfileEditorState, ProfileEditorRoute>(
    bundle = bundle,
    analyticsName = "ProfileEditor",
    initialState = { deps, _ ->
        val profile = deps.profileRepository.self
        ProfileEditorState(
            profile = profile,
            avatarPath = null,
            name = profile?.name,
            isNameValid = (profile?.name?.length ?: 0) > 3,
            isLoading = false,
            hasConnection = deps.connection.isConnectedFlow.value,
            shouldShowImageMenu = false,
        )
    },
) {

    private var updateSelfJob: Job? = null

    init {
        deps.connection.isConnectedFlow.launchCollect { isConnected ->
            state = state.copy(
                hasConnection = isConnected,
            )
        }
    }

    override fun onSystemBackClick() = eventOnSystemBack {
        cancelUpdateAndNavigateBack()
    }

    fun onBackClick() = event("Back", Button, Click) {
        cancelUpdateAndNavigateBack()
    }

    fun onAvatarClick() = event("Avatar", Image, Click) {
        state = state.copy(
            shouldShowImageMenu = true,
        )
    }

    fun onCameraClick() = event("Camera", Button, Click) {
        state = state.copy(
            shouldShowImageMenu = false,
        )
        val isGranted = deps.permissionManager.request(Permission.Camera)
        if (isGranted) {
            state = state.copy(
                avatarPath = deps.imagePicker.takeWithCamera(),
            )
        }
    }

    fun onGalleryClick() = event("Gallery", Button, Click) {
        state = state.copy(
            shouldShowImageMenu = false,
        )
        state = state.copy(
            avatarPath = deps.imagePicker.pickFromGallery(),
        )
    }

    fun onImageMenuDismiss() = event("Image", Menu, Dismiss) {
        state = state.copy(
            shouldShowImageMenu = false,
        )
    }

    fun onNameClick() = event("Name", Field, Click) {
        // no op
    }

    fun onNameChange(name: String) = event("Name", Field, Change) {
        state = state.copy(
            name = name,
            isNameValid = name.length > 3,
        )
    }

    fun onNameDoneClick() = event("NameDone", KeyboardButton, Click) {
        perform(HideKeyboardEffect)
    }

    fun onDoneClick() = event("Done", Button, Click) {
        if (updateSelfJob.isNullOrCompleted) {
            perform(HideKeyboardEffect)

            if (state.name == state.profile?.name && state.avatarPath.isNullOrEmpty()) {
                navigateBack()
                return@event
            }

            updateSelfJob = launch {
                state = state.copy(
                    isLoading = true,
                )

                val result = deps.profileRepository.updateSelf(
                    newName = state.name,
                    newAvatarBytes = state.avatarPath
                        ?.let { path -> deps.imagePicker.loadFile(path) },
                )

                if (result.isSuccess) {
                    navigateBack()
                } else {
                    state = state.copy(
                        isLoading = false,
                    )
                }
            }
        } else {
            updateSelfJob?.cancelAndIfNotFinished {
                state = state.copy(
                    isLoading = false,
                )
            }
        }
    }

    private suspend fun cancelUpdateAndNavigateBack() {
        updateSelfJob?.cancel()
        navigateBack()
    }
}