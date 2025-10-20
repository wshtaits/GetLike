package space.getlike.client_chat

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.design.atoms.DefaultAnimatedVisibility
import space.getlike.client_base.presentation.design.atoms.DefaultIcon
import space.getlike.client_base.presentation.design.atoms.DefaultIconButton
import space.getlike.client_base.presentation.design.atoms.DefaultScaffold
import space.getlike.client_base.presentation.design.atoms.DefaultSpacer
import space.getlike.client_base.presentation.design.atoms.DefaultTopAppBar
import space.getlike.client_base.presentation.design.atoms.DefaultSmallAvatarImage
import space.getlike.client_base.presentation.design.atoms.DefaultSmallButton
import space.getlike.client_base.presentation.design.atoms.DefaultTitle
import space.getlike.client_base.presentation.design.atoms.buttonVisuals
import space.getlike.client_base.presentation.design.molecules.AchievementRow
import space.getlike.client_base.presentation.design.molecules.LikeButton
import space.getlike.client_base.presentation.design.molecules.TotalLikesRow
import space.getlike.client_base.presentation.design.other.OffsetBehavior
import space.getlike.client_base.presentation.design.other.ScrollDirection
import space.getlike.client_base.presentation.design.other.rememberScrollDirection
import space.getlike.client_base.presentation.effects.ScrollLazyListEffect
import space.getlike.models.AchievementContent
import space.getlike.models.Chat
import space.getlike.utils.separatedByThreeChars
import space.getlike.models.Message
import space.getlike.util_core.View
import space.getlike.resources.*
import space.getlike.util_core.utils.invoke
import space.getlike.util_core.utils.rememberMutableStateOf

class ChatView(bundle: Bundle) : View<ChatViewModel, ChatState>(
    bundle = bundle,
    viewModelFactory = ::ChatViewModel,
) {

    @Composable
    override fun Ui() {
        val listState = ScrollLazyListEffect.handle()
        DefaultScaffold(
            topBar = { TopBar(listState) },
            content = { paddingValues -> Content(paddingValues, listState) },
        )
    }

    @Composable
    private fun TopBar(listState: LazyListState) {
        DefaultTopAppBar(
            offsetBehavior = OffsetBehavior.LazyListBottom(listState),
            dividerHideOffset = 8.dp,
            title = { offset ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    DefaultTitle(text = state.chat.contact.name)
                    DefaultSpacer(height = 2.dp)
                    Crossfade(state.chat) { targetState ->
                        TotalLikesRow(
                            totalLikesReceived = targetState.receivedLikesCount,
                            totalLikesSent = targetState.sentLikesCount,
                            shouldShowIcons = true,
                        )
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
            actions = {
                DefaultSmallAvatarImage(
                    avatar = state.chat.contact.avatar,
                    onClick = { viewModel.onAvatarClick() },
                )
                DefaultSpacer(width = 4.dp)
            },
        )
    }

    @Composable
    private fun Content(
        paddingValues: PaddingValues,
        listState: LazyListState,
    ) {
        Box(
            modifier = Modifier
                .padding(paddingValues)
            ,
        ) {
            ChatColumn(listState)

            NotContactRow(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                ,
                listState = listState,
            )

            LikeButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                ,
                maxLikesCount = Chat.MAX_LIKES_COUNT - state.chat.sentLikesCount,
                onConfirm = { likesCount -> viewModel.onSendLikes(likesCount) },
            )
        }
    }

    @Composable
    private fun NotContactRow(
        modifier: Modifier,
        listState: LazyListState,
    ) {
        val scrollDirection by listState.rememberScrollDirection()

        DefaultAnimatedVisibility(
            modifier = modifier,
            visible = state.chat.contact.isNotContact && scrollDirection != ScrollDirection.DOWN,
            fade = true,
            slideVertically = true,
            slideVerticallyOffset = { fullHeight -> -fullHeight }
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .background(
                        color = AppTheme.colors.background,
                        shape = RoundedCornerShape(16.dp),
                    )
                    .border(
                        width = 1.dp,
                        color = AppTheme.colors.secondary(0.05f),
                        shape = RoundedCornerShape(16.dp),
                    )
                    .padding(vertical = 12.dp, horizontal = 16.dp)
                ,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.chat_not_contact_label),
                    color = AppTheme.colors.secondary,
                    style = AppTheme.typography.labelMedium,
                )

                DefaultSpacer(width = 8.dp)

                DefaultSmallButton(
                    visuals = if (state.isAddingContact) {
                        buttonVisuals(
                            loading = true,
                            enabled = false,
                        )
                    } else {
                        buttonVisuals(
                            textRes = Res.string.chat_add_button,
                        )
                    },
                    onClick = { viewModel.onAddContactClick() },
                )
            }
        }
    }

    @Composable
    private fun ChatColumn(listState: LazyListState) {
        val shownMessageIds = remember {
            state.chat.messages
                .map { message -> message.id }
                .toMutableSet()
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
            ,
            state = listState,
            reverseLayout = true,
            contentPadding = PaddingValues(top = 8.dp, bottom = 104.dp),
        ) {
            items(
                items = state.chat.messages
                    .sortedBy { message -> message.timestamp }
                    .reversed(),
                key = { message -> message.id },
            ) { message ->
                val isNew = remember(message.id) { !shownMessageIds.contains(message.id) }

                LaunchedEffect(message.id) {
                    shownMessageIds.add(message.id)
                }

                Message(
                    modifier = Modifier
                        .animateItem(placementSpec = tween(400))
                    ,
                    message = message,
                    isNew = isNew,
                )
            }

            item {
                AchievementRow(
                    content = AchievementContent.GoalFriend,
                    asHint = true,
                )
            }
        }
    }

    @Composable
    private fun Message(
        modifier: Modifier,
        message: Message,
        isNew: Boolean,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
            ,
        ) {
            var visible by rememberMutableStateOf(!isNew || message.senderId == state.chat.contact.id)

            LaunchedEffect(message.id) {
                visible = true
            }

            val visuals = when {
                message.senderId == state.chat.contact.id ->
                    MessageVisuals.contact(message.likesCount)
                message.isUnsent ->
                    MessageVisuals.unsent(message.likesCount)
                message.isUnread ->
                    MessageVisuals.unread(message.likesCount)
                else ->
                    MessageVisuals.read(message.likesCount)
            }

            val contentColor by animateColorAsState(visuals.contentColor)
            val backgroundColor by animateColorAsState(visuals.backgroundColor)
            val borderColor by animateColorAsState(visuals.borderColor)

            DefaultAnimatedVisibility(
                modifier = Modifier
                    .align(visuals.alignment)
                ,
                visible = visible,
                delay = 400,
                easing = CubicBezierEasing(0.0f, 0.5f, 0.5f, 1.0f),
                slideHorizontally = true,
                slideHorizontallyOffset = { fullWidth -> fullWidth },
            ) {
                Row(
                    modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 16.dp)
                    .background(
                        color = backgroundColor,
                        shape = visuals.shape,
                    )
                    .border(
                        width = 2.dp,
                        color = borderColor,
                        shape = visuals.shape,
                    )
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Crossfade(visuals.iconRes) { targetState ->
                        DefaultIcon(
                            modifier = Modifier
                                .size(24.dp)
                            ,
                            imageRes = targetState,
                            descriptionRes = Res.string.chat_like_description,
                            tint = contentColor,
                        )
                    }


                    DefaultSpacer(width = 4.dp)

                    Text(
                        text = visuals.likesCount.separatedByThreeChars(),
                        color = contentColor,
                        style = AppTheme.typography.titleMedium,
                    )
                }
            }
        }
    }

    private class MessageVisuals(
        val alignment: Alignment,
        val likesCount: Int,
        val iconRes: DrawableResource,
        val contentColor: Color,
        val borderColor: Color,
        val backgroundColor: Color,
        radiusBottomStart: Dp,
        radiusBottomEnd: Dp,
    ) {

        val shape: Shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
            bottomStart = radiusBottomStart,
            bottomEnd = radiusBottomEnd,
        )

        companion object {

            @Composable
            fun contact(likesCount: Int): MessageVisuals =
                MessageVisuals(
                    alignment = Alignment.BottomStart,
                    likesCount = likesCount,
                    iconRes = Res.drawable.ic_heart_filled,
                    contentColor = AppTheme.colors.accent,
                    borderColor = Color.Transparent,
                    backgroundColor = AppTheme.colors.accent(0.1f),
                    radiusBottomStart = 0.dp,
                    radiusBottomEnd = 20.dp,
                )

            @Composable
            fun unsent(likesCount: Int): MessageVisuals =
                MessageVisuals(
                    alignment = Alignment.BottomEnd,
                    likesCount = likesCount,
                    iconRes = Res.drawable.ic_heart,
                    contentColor = AppTheme.colors.primary,
                    borderColor = AppTheme.colors.primary,
                    backgroundColor = AppTheme.colors.background,
                    radiusBottomStart = 20.dp,
                    radiusBottomEnd = 0.dp,
                )

            @Composable
            fun unread(likesCount: Int): MessageVisuals =
                MessageVisuals(
                    alignment = Alignment.BottomEnd,
                    likesCount = likesCount,
                    iconRes = Res.drawable.ic_heart_filled,
                    contentColor = AppTheme.colors.background,
                    borderColor = Color.Transparent,
                    backgroundColor = AppTheme.colors.primary,
                    radiusBottomStart = 20.dp,
                    radiusBottomEnd = 0.dp,
                )

            @Composable
            fun read(likesCount: Int): MessageVisuals =
                MessageVisuals(
                    alignment = Alignment.BottomEnd,
                    likesCount = likesCount,
                    iconRes = Res.drawable.ic_heart_filled,
                    contentColor = AppTheme.colors.primary,
                    borderColor = Color.Transparent,
                    backgroundColor = AppTheme.colors.primary(0.1f),
                    radiusBottomStart = 20.dp,
                    radiusBottomEnd = 0.dp,
                )
        }
    }
}