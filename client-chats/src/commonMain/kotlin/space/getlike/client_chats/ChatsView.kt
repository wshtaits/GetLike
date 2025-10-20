package space.getlike.client_chats

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.design.atoms.DefaultBottomHideableHorizonalDivider
import space.getlike.client_base.presentation.design.atoms.DefaultIcon
import space.getlike.client_base.presentation.design.atoms.DefaultScrollbar
import space.getlike.client_base.presentation.design.atoms.DefaultSpacer
import space.getlike.client_base.presentation.design.atoms.DefaultTopAppBar
import space.getlike.client_base.presentation.design.atoms.DefaultMediumAvatarImage
import space.getlike.client_base.presentation.design.atoms.toTitleState
import space.getlike.client_base.presentation.design.other.OffsetBehavior
import space.getlike.models.Chat
import space.getlike.util_core.utils.toSp
import space.getlike.client_base.presentation.utils.toTimePassedString
import space.getlike.util_core.View
import space.getlike.resources.*
import space.getlike.util_core.utils.rippleClickable
import space.getlike.util_core.utils.invoke
import space.getlike.utils.separatedByThreeChars

class ChatsView(bundle: Bundle) : View<ChatsViewModel, ChatsState>(
    bundle = bundle,
    viewModelFactory = ::ChatsViewModel,
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
    private fun TopBar(listState: LazyListState) {
        DefaultTopAppBar(
            titleRes = Res.string.chats_title,
            state = state.syncState.toTitleState(),
            offsetBehavior = OffsetBehavior.LazyListTop(listState),
            dividerHideOffset = 8.dp,
            removeWindowInsets = true,
        )
    }

    @Composable
    private fun Content(listState: LazyListState) {
        val chats = remember(state.chats) {
            state.chats
                .filter { chat -> chat.latestTimestamp != null }
                .sortedByDescending { chat -> chat.latestTimestamp }
        }
        if (chats.isEmpty()) {
            Placeholder()
        } else {
            ChatsColumn(listState, chats)
        }
    }

    @Composable
    private fun Placeholder() {
        Box(
            modifier = Modifier
                .fillMaxSize()
            ,
            contentAlignment = Alignment.Center
        ) {
            Column(
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
                        text = "🙁",
                        fontSize = 64.dp.toSp(),
                    )
                }

                DefaultSpacer(height = 40.dp)

                Text(
                    text = stringResource(Res.string.chats_empty_label),
                    color = AppTheme.colors.secondary,
                    style = AppTheme.typography.titleLarge,
                )

                DefaultSpacer(height = 4.dp)

                Text(
                    text = stringResource(Res.string.chats_empty_hint),
                    color = AppTheme.colors.secondary(0.7f),
                    style = AppTheme.typography.labelLarge,
                )
            }

            Image(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth(0.33f)
                    .padding(bottom = 24.dp, end = 24.dp)
                ,
                imageVector = vectorResource(Res.drawable.vect_pointer),
                contentDescription = stringResource(Res.string.chats_description_pointer),
            )
        }
    }

    @Composable
    private fun ChatsColumn(
        listState: LazyListState,
        chats: List<Chat>,
    ) {
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
                    items(chats) { chat ->
                        ChatRow(chat)
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
    private fun ChatRow(chat: Chat) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .rippleClickable(
                    color = AppTheme.colors.primary(0.02f),
                    onClick = { viewModel.onChatClick(chat) },
                )
                .padding(vertical = 8.dp, horizontal = 16.dp)
            ,
        ) {
            DefaultMediumAvatarImage(chat.contact.avatar)

            DefaultSpacer(width = 12.dp)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                ,
            ) {
                Text(
                    text = chat.contact.name,
                    color = AppTheme.colors.secondary,
                    style = AppTheme.typography.buttonLarge,
                )

                DefaultSpacer(height = 4.dp)

                MessagesRow(chat)
            }

            DefaultSpacer(width = 12.dp)

            Text(
                modifier = Modifier
                    .padding(top = 4.dp)
                ,
                text = chat.latestTimestamp?.toTimePassedString().orEmpty(),
                color = AppTheme.colors.tertiary,
                style = AppTheme.typography.labelSmall,
            )
        }
    }

    @Composable
    private fun MessagesRow(chat: Chat) {
        Crossfade(
            targetState = remember(chat) { calculateMessagesState(chat) },
        ) { targetState ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Message(
                    text = targetState.likesSent.separatedByThreeChars(),
                    iconRes = Res.drawable.ic_heart_outgoing,
                    contentColor = AppTheme.colors.primary(0.8f),
                    containerColor = AppTheme.colors.primary(0.2f),
                    borderColor = Color.Transparent,
                )

                if (targetState.newLikesSent != 0) {
                    val (contentColor, containerColor, borderColor) = if (targetState.hasUnsentMessages) {
                        Triple(
                            AppTheme.colors.primary,
                            AppTheme.colors.background,
                            AppTheme.colors.primary,
                        )
                    } else {
                        Triple(
                            AppTheme.colors.background,
                            AppTheme.colors.primary,
                            Color.Transparent,
                        )
                    }

                    DefaultSpacer(width = 4.dp)

                    Message(
                        text = "+ ${targetState.newLikesSent.separatedByThreeChars()}",
                        iconRes = null,
                        contentColor = contentColor,
                        containerColor = containerColor,
                        borderColor = borderColor,
                    )
                }

                DefaultSpacer(width = 4.dp)

                Message(
                    text = targetState.likesReceived.separatedByThreeChars(),
                    iconRes = Res.drawable.ic_heart_incoming,
                    contentColor = AppTheme.colors.accent(0.8f),
                    containerColor = AppTheme.colors.accent(0.2f),
                    borderColor = Color.Transparent,
                )

                if (targetState.newLikesReceived != 0) {
                    DefaultSpacer(width = 4.dp)

                    Message(
                        text = "+ ${targetState.newLikesReceived.separatedByThreeChars()}",
                        iconRes = null,
                        contentColor = AppTheme.colors.background,
                        containerColor = AppTheme.colors.accent,
                        borderColor = Color.Transparent,
                    )
                }
            }
        }
    }

    @Composable
    private fun Message(
        text: String,
        iconRes: DrawableResource?,
        contentColor: Color,
        containerColor: Color,
        borderColor: Color,
    ) {
        val shape = RoundedCornerShape(8.dp)
        Row(
            modifier = Modifier
                .background(
                    color = containerColor,
                    shape = RoundedCornerShape(8.dp),
                )
                .border(
                    color = borderColor,
                    shape = shape,
                    width = 1.5.dp,
                )
                .padding(
                    vertical = 2.dp,
                    horizontal = 8.dp,
                )
            ,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (iconRes != null) {
                DefaultIcon(
                    modifier = Modifier
                        .size(16.dp)
                    ,
                    imageRes = iconRes,
                    descriptionRes = Res.string.chats_description_likes,
                    tint = contentColor,
                )

                DefaultSpacer(width = 4.dp)
            }

            Text(
                text = text,
                color = contentColor,
                style = AppTheme.typography.labelMedium,
            )
        }
    }

    private fun calculateMessagesState(chat: Chat): MessagesState {
        val notReadMessages = chat.messages.filter { message -> !message.isRead }
        return MessagesState(
            likesReceived = chat.receivedLikesCount,
            likesSent = chat.sentLikesCount,
            newLikesSent = notReadMessages
                .filter { message -> message.senderId != chat.contact.id }
                .sumOf { message -> message.likesCount },
            newLikesReceived = notReadMessages
                .filter { message -> message.senderId == chat.contact.id }
                .sumOf { message -> message.likesCount },
            hasUnsentMessages = notReadMessages
                .any { message -> message.isUnsent},
        )
    }

    private data class MessagesState(
        val likesReceived: Int,
        val likesSent: Int,
        val newLikesReceived: Int,
        val newLikesSent: Int,
        val hasUnsentMessages: Boolean,
    )
}