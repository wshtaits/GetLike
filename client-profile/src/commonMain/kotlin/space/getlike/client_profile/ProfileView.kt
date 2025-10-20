package space.getlike.client_profile

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.design.atoms.DefaultAnimatedVisibility
import space.getlike.client_base.presentation.design.atoms.DefaultBottomHideableHorizonalDivider
import space.getlike.client_base.presentation.design.atoms.DefaultIconButton
import space.getlike.client_base.presentation.design.atoms.DefaultSpacer
import space.getlike.client_base.presentation.design.atoms.DefaultTitle
import space.getlike.client_base.presentation.design.atoms.DefaultTextButton
import space.getlike.client_base.presentation.design.atoms.DefaultTopAppBar
import space.getlike.client_base.presentation.design.atoms.toTitleState
import space.getlike.client_base.presentation.design.molecules.TotalLikesRow
import space.getlike.client_base.presentation.design.molecules.ProfileColumn
import space.getlike.client_base.presentation.design.other.OffsetBehavior
import space.getlike.resources.*
import space.getlike.util_core.View

class ProfileView(bundle: Bundle) : View<ProfileViewModel, ProfileState>(
    bundle = bundle,
    viewModelFactory = ::ProfileViewModel,
) {

    @Composable
    override fun Ui() {
        Column(
            modifier = Modifier
                .fillMaxSize()
            ,
        ) {
            val scrollState = rememberScrollState()
            TopBar(scrollState)
            Content(scrollState)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TopBar(
        scrollState: ScrollState,
    ) {
        DefaultTopAppBar(
            offsetBehavior = OffsetBehavior.ScrollTop(scrollState),
            dividerHideOffset = 60.dp,
            removeWindowInsets = true,
            title = { offset ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    DefaultTitle(
                        textRes = Res.string.profile_title,
                        state = state.syncState.toTitleState(),
                    )

                    DefaultAnimatedVisibility(
                        duration = 250,
                        visible = offset > 410.dp,
                        fade = true,
                        scale = true,
                        expand = true,
                    ) {
                        Column {
                            DefaultSpacer(height = 2.dp)
                            TotalLikesRow(
                                totalLikesReceived = state.profile.totalLikesReceived,
                                totalLikesSent = state.profile.totalLikesSent,
                                shouldShowIcons = true,
                            )
                        }
                    }
                }
            },
            navigationIcon = {
                DefaultTextButton(
                    textRes = Res.string.profile_edit_button,
                    onClick = { viewModel.onEditClick() },
                )
            },
            actions = {
                DefaultIconButton(
                    imageRes = Res.drawable.ic_logout,
                    descriptionRes = Res.string.profile_description_logout,
                    tint = AppTheme.colors.error,
                    onClick = { viewModel.onLogoutClick() },
                )
            },
        )
    }

    @Composable
    private fun Content(scrollState: ScrollState) {
        Box {
            ProfileColumn(
                scrollState = scrollState,
                profile = state.profile,
                onShareClick = { viewModel.onShareClick() },
            )
            DefaultBottomHideableHorizonalDivider(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                ,
                state = scrollState,
                hideOffset = 16.dp,
            )
        }
    }
}