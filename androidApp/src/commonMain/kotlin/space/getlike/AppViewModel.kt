package space.getlike

import space.getlike.client_base.dependencies.PresentationDependencies
import space.getlike.client_base.presentation.routes.LoginRoute
import space.getlike.client_base.presentation.routes.MainRoute
import space.getlike.client_base.presentation.routes.ProfileEditorRoute
import space.getlike.util_core.CoreAppViewModel

class AppViewModel(bundle: Bundle) : CoreAppViewModel<PresentationDependencies>(bundle) {

    init {
        deps.loginRepository.isAuthedFlow.launchCollect { isAuthed ->
            if (!isAuthed) {
                deps.loginRepository.logout()
                navigateRoot(LoginRoute(), popAll = true)
            }
        }
        onResult<LoginRoute>(::onLoginResult)
        onResult<ProfileEditorRoute>(::onProfileEditorResult)
        onResult<MainRoute>(::onMainResult)
    }

    override fun onLaunch() {
        launch {
            deps.appShortcuts.setup(deps.appShortcutItems)

            deps.logger.logAppLaunch()

            deps.syncRepository.init()

            when {
                !deps.loginRepository.isAuthedFlow.value ->
                    navigateRoot(LoginRoute(), replace = true)

                deps.profileRepository.self == null -> {
                    sync()
                    if (deps.profileRepository.self == null) {
                        navigateRoot(ProfileEditorRoute(), replace = true)
                    } else {
                        navigateRoot(MainRoute(), replace = true)
                    }
                }

                else -> {
                    navigateRoot(MainRoute(), replace = true)
                    sync()
                }
            }
        }
    }

    private suspend fun onLoginResult() {
        when {
            deps.profileRepository.self == null ->
                deps.appLifecycle.close()
            deps.profileRepository.self?.name.isNullOrEmpty() ->
                navigateRoot(ProfileEditorRoute(), replace = true)
            else ->
                navigateRoot(MainRoute(), replace = true)
        }
    }

    private suspend fun onProfileEditorResult() {
        if (deps.profileRepository.self?.name.isNullOrEmpty()) {
            deps.loginRepository.logout()
            navigateRoot(LoginRoute())
        } else {
            navigateRoot(MainRoute())
        }
    }

    private fun onMainResult() =
        deps.appLifecycle.close()

    private suspend fun sync() {
        deps.syncRepository.sync()
        deps.syncRepository.enableAutoSync()
    }
}