package space.getlike.client_main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.design.atoms.DefaultNavigationBar
import space.getlike.client_base.presentation.design.atoms.DefaultNavigationItem
import space.getlike.client_base.presentation.design.atoms.DefaultNavigationItemIcon
import space.getlike.client_base.presentation.design.atoms.DefaultScaffold
import space.getlike.client_base.presentation.design.atoms.DefaultSnackbarHost
import space.getlike.client_base.presentation.effects.ShowSnackbarEffect
import space.getlike.client_base.presentation.routes.ChatsRoute
import space.getlike.client_chats.ChatsView
import space.getlike.client_base.presentation.routes.ContactsRoute
import space.getlike.client_contacts.ContactsView
import space.getlike.client_base.presentation.routes.ProfileRoute
import space.getlike.client_profile.ProfileView
import space.getlike.util_core.View
import space.getlike.resources.*
import space.getlike.util_core.interceptSystemBack
import space.getlike.util_core.navigatesTo

class MainView(bundle: Bundle) : View<MainViewModel, MainState>(
    bundle = bundle,
    viewModelFactory = ::MainViewModel,
    initialChildBackStackRoute = ProfileRoute(),
) {

    @Composable
    override fun Ui() {
        DefaultScaffold(
            content = { paddingValues -> Content(paddingValues) },
            bottomBar = { BottomBar() },
        )
    }

    @Composable
    private fun Content(
        paddingValues: PaddingValues,
    ) {
        Box {
            ChildNavDisplay(
                modifier = Modifier
                    .padding(paddingValues)
                ,
                routing = listOf(
                    ProfileRoute::class navigatesTo ::ProfileView.interceptSystemBack(),
                    ContactsRoute::class navigatesTo ::ContactsView.interceptSystemBack(),
                    ChatsRoute::class navigatesTo ::ChatsView.interceptSystemBack(),
                ),
            )

            val snackbarHostState = ShowSnackbarEffect.handle()
            DefaultSnackbarHost(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(paddingValues)
                ,
                state = snackbarHostState,
            )
        }
    }

    @Composable
    private fun BottomBar() {
        DefaultNavigationBar {
            DefaultNavigationItem(
                selected = state.isProfileSelected,
                selectedIconRes = Res.drawable.ic_person_filled,
                unselectedIconRes = Res.drawable.ic_person,
                descriptionRes = Res.string.main_profile,
                onClick = { viewModel.onProfileClick() },
            )

            DefaultNavigationItem(
                icon = {
                    Box {
                        DefaultNavigationItemIcon(
                            selected = state.isChatsSelected,
                            selectedIconRes = Res.drawable.ic_message_filled,
                            unselectedIconRes = Res.drawable.ic_message,
                            descriptionRes = Res.string.main_chats,
                        )
                        if (state.hasUnreadMessages) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .align(Alignment.TopEnd)
                                    .background(
                                        color = AppTheme.colors.accent,
                                        shape = CircleShape
                                    )
                                ,
                            )
                        }
                    }
                },
                onClick = { viewModel.onChatsClick() },
            )

            DefaultNavigationItem(
                selected = state.isContactsSelected,
                selectedIconRes = Res.drawable.ic_contacts_filled,
                unselectedIconRes = Res.drawable.ic_contacts,
                descriptionRes = Res.string.main_contacts,
                onClick = { viewModel.onContactsClick() },
            )
        }
    }
}
