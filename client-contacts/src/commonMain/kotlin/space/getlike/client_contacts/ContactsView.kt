package space.getlike.client_contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.design.atoms.DefaultAnimatedContent
import space.getlike.client_base.presentation.design.atoms.DefaultBottomHideableHorizonalDivider
import space.getlike.client_base.presentation.design.atoms.DefaultIconButton
import space.getlike.client_base.presentation.design.atoms.DefaultLargeButton
import space.getlike.client_base.presentation.design.atoms.DefaultMediumAvatarImage
import space.getlike.client_base.presentation.design.atoms.DefaultScrollbar
import space.getlike.client_base.presentation.design.atoms.DefaultSmallButton
import space.getlike.client_base.presentation.design.atoms.DefaultSpacer
import space.getlike.client_base.presentation.design.atoms.DefaultTopAppBar
import space.getlike.client_base.presentation.design.atoms.toTitleState
import space.getlike.client_base.presentation.design.molecules.TotalLikesRow
import space.getlike.client_base.presentation.design.other.OffsetBehavior
import space.getlike.client_base.presentation.design.other.styledStringArrayResource
import space.getlike.models.Avatar
import space.getlike.resources.*
import space.getlike.util_core.View
import space.getlike.util_core.utils.applyIfNotNull
import space.getlike.util_core.utils.rippleClickable
import space.getlike.util_core.utils.invoke
import space.getlike.util_core.utils.toSp

class ContactsView(bundle: Bundle) : View<ContactsViewModel, ContactsState>(
    bundle = bundle,
    viewModelFactory = ::ContactsViewModel,
) {

    @Composable
    override fun Ui() {
        Column(
            modifier = Modifier
                .fillMaxSize()
            ,
        ) {
            val listState = rememberLazyListState()
            TopBar(listState)
            Content(listState)
        }
    }

    @Composable
    private fun TopBar(
        listState: LazyListState,
    ) {
        DefaultTopAppBar(
            titleRes = Res.string.contacts_title,
            state = state.syncState.toTitleState(),
            offsetBehavior = OffsetBehavior.LazyListTop(listState),
            dividerHideOffset = 8.dp,
            removeWindowInsets = true,
            navigationIcon = {
                DefaultAnimatedContent(
                    targetState = when {
                        state.isLoadingLocalContacts -> true
                        state.hasContactsPermission -> null
                        else -> false
                    },
                    scale = true,
                ) { targetState ->
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                        ,
                    ) {
                        when (targetState) {
                            true ->
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .padding(horizontal = 14.dp)
                                        .size(20.dp),
                                    strokeWidth = 3.dp,
                                    color = AppTheme.colors.secondary,
                                )
                            false -> DefaultIconButton(
                                imageRes = Res.drawable.ic_group_add,
                                descriptionRes = Res.string.contacts_description_import_contacts,
                                onClick = { viewModel.onImportContactsActionClick() },
                            )
                            else -> { /* no op */ }
                        }
                    }
                }
            },
            actions = {
                DefaultIconButton(
                    imageRes = Res.drawable.ic_person_search,
                    descriptionRes = Res.string.contacts_description_search_contact,
                    onClick = { viewModel.onSearchContactActionClick() },
                )
            }
        )
    }

    @Composable
    private fun Content(listState: LazyListState) {
        if (state.chats.isEmpty() && state.localContacts.isEmpty()) {
            Placeholder()
        } else {
            ContactsColumn(listState)
        }
    }

    @Composable
    private fun Placeholder() {
        Column(
            modifier = Modifier
                .fillMaxSize()
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = AppTheme.colors.placeholder,
                        shape = RoundedCornerShape(52.dp),
                    )
                    .size(140.dp)
                ,
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                    ,
                    text = "\uD83D\uDE0E",
                    fontSize = 64.dp.toSp(),
                )
            }
            DefaultSpacer(height = 40.dp)
            Text(
                text = stringResource(Res.string.contacts_empty_label),
                color = AppTheme.colors.secondary,
                style = AppTheme.typography.titleLarge,
            )
            DefaultSpacer(height = 8.dp)
            Text(
                text = styledStringArrayResource(
                    res = Res.array.contacts_empty_hint,
                    style = SpanStyle(fontWeight = FontWeight.W600),
                ),
                color = AppTheme.colors.secondary(0.7f),
                style = AppTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
            )
            DefaultSpacer(height = 44.dp)
            DefaultLargeButton(
                textRes = Res.string.contacts_empty_button,
                enabled = !state.isLoadingLocalContacts,
                loading = state.isLoadingLocalContacts,
                onClick = { viewModel.onImportContactsClick() },
            )
        }
    }

    @Composable
    private fun ContactsColumn(listState: LazyListState) {
        Box {
            DefaultScrollbar(
                state = listState,
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                    ,
                    state = listState,
                ) {
                    items(state.chats) { chat ->
                        ContactRow(
                            contact = chat,
                            avatar = chat.contact.avatar,
                            name = chat.contact.name,
                            label = {
                                TotalLikesRow(
                                    totalLikesReceived = chat.contact.totalLikesReceived,
                                    totalLikesSent = chat.contact.totalLikesSent,
                                    shouldShowIcons = false,
                                )
                            },
                            buttonTextRes = Res.string.contacts_like_button,
                            buttonColor = AppTheme.colors.primary,
                            onContactClick = { viewModel.onContactClick(chat) },
                            onContactActionClick = { contact -> viewModel.onContactLikeClick(contact) },
                        )
                    }
                    items(state.localContacts) { localContact ->
                        ContactRow(
                            contact = localContact,
                            avatar = localContact.avatar,
                            name = localContact.name,
                            label = {
                                Text(
                                    text = stringResource(Res.string.contacts_not_in_app_hint),
                                    color = AppTheme.colors.secondary(0.7f),
                                    style = AppTheme.typography.labelMedium,
                                )
                            },
                            buttonTextRes = Res.string.contacts_invite_button,
                            buttonColor = AppTheme.colors.accent,
                            onContactClick = null,
                            onContactActionClick = { localContact ->
                                viewModel.onLocalContactInviteClick(localContact)
                            },
                        )
                    }
                }
            }
            DefaultBottomHideableHorizonalDivider(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                ,
                state = listState,
                hideOffset = 8.dp
            )
        }
    }

    @Composable
    private fun <T> ContactRow(
        contact: T,
        avatar: Avatar,
        name: String,
        buttonTextRes: StringResource,
        buttonColor: Color,
        label: @Composable () -> Unit,
        onContactClick: ((T) -> Unit)?,
        onContactActionClick: (T) -> Unit,
    ) {
        Row(
            modifier = Modifier
                .applyIfNotNull(onContactClick) { onContactClick ->
                    rippleClickable(
                        color = buttonColor(0.02f),
                        onClick = { onContactClick(contact) },
                    )
                }
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DefaultMediumAvatarImage(avatar)
            DefaultSpacer(width = 12.dp)
            Column(
                modifier = Modifier
                    .weight(1f)
                ,
            ) {
                Text(
                    text = name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = AppTheme.colors.secondary,
                    style = AppTheme.typography.buttonLarge,
                )

                DefaultSpacer(height = 6.dp)

                label()
            }
            DefaultSpacer(width = 12.dp)
            DefaultSmallButton(
                textRes = buttonTextRes,
                color = buttonColor,
                onClick = { onContactActionClick(contact) },
            )
        }
    }
}