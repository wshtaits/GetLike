package space.getlike.client_contact

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import space.getlike.client_base.presentation.design.atoms.DefaultAnimatedVisibility
import space.getlike.client_base.presentation.design.atoms.DefaultIconButton
import space.getlike.client_base.presentation.design.atoms.DefaultScaffold
import space.getlike.client_base.presentation.design.atoms.DefaultSpacer
import space.getlike.client_base.presentation.design.atoms.DefaultTitle
import space.getlike.client_base.presentation.design.atoms.DefaultTopAppBar
import space.getlike.client_base.presentation.design.molecules.ProfileColumn
import space.getlike.client_base.presentation.design.molecules.TotalLikesRow
import space.getlike.client_base.presentation.design.other.OffsetBehavior
import space.getlike.util_core.View
import space.getlike.resources.*

class ContactView(bundle: Bundle) : View<ContactViewModel, ContactState>(
    bundle = bundle,
    viewModelFactory = ::ContactViewModel,
) {

    @Composable
    override fun Ui() {
        val scrollState = rememberScrollState()
        DefaultScaffold(
            topBar = { TopBar(scrollState) },
            content = { paddingValues -> Content(paddingValues, scrollState) },
        )
    }

    @Composable
    private fun TopBar(
        scrollState: ScrollState,
    ) {
        DefaultTopAppBar(
            offsetBehavior = OffsetBehavior.ScrollTop(scrollState),
            dividerHideOffset = 60.dp,
            title = { offset ->
                DefaultAnimatedVisibility(
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    visible = offset > 226.dp,
                    duration = 250,
                    fade = true,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 26.dp)
                        ,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        DefaultTitle(
                            text = state.chat?.contact?.name.orEmpty(),
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
                                    totalLikesReceived = state.chat?.contact?.totalLikesReceived ?: 0,
                                    totalLikesSent = state.chat?.contact?.totalLikesSent ?: 0,
                                    shouldShowIcons = true,
                                )
                            }
                        }
                    }
                }
            },
            navigationIcon = {
                DefaultIconButton(
                    imageRes = Res.drawable.ic_back,
                    descriptionRes = Res.string.common_description_back,
                    onClick = { viewModel.onBackClick() },
                )
            },
        )
    }

    @Composable
    private fun Content(
        paddingValues: PaddingValues,
        scrollState: ScrollState,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
            ,
            contentAlignment = Alignment.Center,
        ) {
            val chat = state.chat
            if (chat == null) {
                CircularProgressIndicator()
            } else {
                ProfileColumn(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                    ,
                    scrollState = scrollState,
                    profile = chat.contact,
                    isAddingContact = state.isAddingContact,
                    onAddContactClick = { viewModel.onAddContactClick() },
                    onChatClick = { viewModel.onChatClick() },
                    onShareClick = { viewModel.onShareClick() },
                )
            }
        }
    }
}