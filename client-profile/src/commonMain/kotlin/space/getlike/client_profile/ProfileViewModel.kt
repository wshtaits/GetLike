package space.getlike.client_profile

import space.getlike.client_base.presentation.routes.ProfileRoute
import space.getlike.client_base.presentation.viewmodel.ViewModel
import space.getlike.client_base.presentation.routes.ProfileEditorRoute
import space.getlike.deeplinks.ProfileDeeplink

class ProfileViewModel(
    bundle: Bundle,
) : ViewModel<ProfileState, ProfileRoute>(
    bundle = bundle,
    analyticsName = "Profile",
    initialState = { deps, _ ->
        ProfileState(
            profile = deps.profileRepository.self!!,
            syncState = deps.syncRepository.syncState,
        )
    },
) {

    init {
        deps.profileRepository.nonNullSelfProfileFlow.launchCollect { profile ->
            state = state.copy(profile = profile)
        }
        deps.syncRepository.syncStateFlow.launchCollect { syncState ->
            state = state.copy(syncState = syncState)
        }
    }

    fun onEditClick() = event("Edit", Button, Click) {
        navigateRoot(ProfileEditorRoute())
    }

    fun onShareClick() = event("Share", Button, Click) {
        deps.share.text(ProfileDeeplink(state.profile.id))
    }

    fun onLogoutClick() = event("Logout", Button, Click) {
        deps.loginRepository.logout()
    }
}