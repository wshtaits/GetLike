package space.getlike

import androidx.compose.runtime.*
import space.getlike.client_base.dependencies.Dependencies
import space.getlike.client_base.dependencies.PlatformDependenciesFactory
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.routes.ChatRoute
import space.getlike.client_chat.ChatView
import space.getlike.client_base.presentation.routes.ContactRoute
import space.getlike.client_contact.ContactView
import space.getlike.client_base.presentation.routes.LoginRoute
import space.getlike.client_login.LoginView
import space.getlike.client_base.presentation.routes.MainRoute
import space.getlike.client_main.MainView
import space.getlike.client_base.presentation.routes.SearchContactRoute
import space.getlike.client_search_contact.SearchContactView
import space.getlike.client_base.presentation.routes.ProfileEditorRoute
import space.getlike.client_profile_editor.ProfileEditorView
import space.getlike.client_base.presentation.routes.InviteRoute
import space.getlike.client_invite.InviteView
import space.getlike.client_base.presentation.routes.SplashRoute
import space.getlike.client_splash.SplashView
import space.getlike.client_base.presentation.routes.TermsRoute
import space.getlike.client_terms.TermsView
import space.getlike.util_core.Root
import space.getlike.util_core.navigatesTo

@Composable
fun App(platformDependenciesFactory: PlatformDependenciesFactory) {
    AppTheme {
        Root(
            dependenciesFactory = { savedStateHandle, appViewModelScope ->
                Dependencies(platformDependenciesFactory, savedStateHandle, appViewModelScope)
            },
            appViewModelFactory = ::AppViewModel,
            initialRoute = SplashRoute(),
            navigations = listOf(
                ChatRoute::class navigatesTo ::ChatView,
                ContactRoute::class navigatesTo ::ContactView,
                LoginRoute::class navigatesTo ::LoginView,
                MainRoute::class navigatesTo ::MainView,
                SearchContactRoute::class navigatesTo ::SearchContactView,
                ProfileEditorRoute::class navigatesTo ::ProfileEditorView,
                InviteRoute::class navigatesTo ::InviteView,
                SplashRoute::class navigatesTo ::SplashView,
                TermsRoute::class navigatesTo ::TermsView,
            ),
        )
    }
}
