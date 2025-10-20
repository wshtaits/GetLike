package space.getlike.client_base.presentation.design.molecules

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.design.atoms.DefaultIcon
import space.getlike.client_base.presentation.design.atoms.DefaultScrollbar
import space.getlike.client_base.presentation.design.atoms.DefaultSpacer
import space.getlike.client_base.presentation.design.atoms.DefaultLargeAvatarImage
import space.getlike.utils.separatedByThreeChars
import space.getlike.models.AchievementContent
import space.getlike.models.Profile
import space.getlike.resources.*
import space.getlike.util_core.utils.Quadruple
import space.getlike.util_core.utils.invoke

@Composable
fun ProfileColumn(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    profile: Profile,
    isAddingContact: Boolean = false,
    onAddContactClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onShareClick: () -> Unit,
) {
    DefaultScrollbar(
        state = scrollState,
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DefaultSpacer(height = 60.dp)

            AvatarAndActionsRow(
                profile,
                isAddingContact,
                onAddContactClick,
                onChatClick,
                onShareClick,
            )

            DefaultSpacer(height = 28.dp)

            Text(
                text = profile.name,
                color = AppTheme.colors.secondary,
                style = AppTheme.typography.titleLarge,
            )

            DefaultSpacer(height = 50.dp)

            TotalLikesRow(
                totalLikes = profile.totalLikesReceived,
                color = AppTheme.colors.accent,
                textRes = Res.string.total_likes_row_received,
                icon = Res.drawable.ic_heart_incoming,
                topStart = 16.dp,
                topEnd = 16.dp,
            )
            TotalLikesRow(
                totalLikes = profile.totalLikesSent,
                color = AppTheme.colors.primary,
                textRes = Res.string.total_likes_row_sent,
                icon = Res.drawable.ic_heart_outgoing,
                bottomStart = 16.dp,
                bottomEnd = 16.dp,
            )

            DefaultSpacer(height = 24.dp)

            val content = if (profile.isSelf) {
                AchievementContent.Goal
            } else {
                AchievementContent.GoalFriend
            }
            AchievementRow(content)

            val sortedAchievements = remember(profile.achievements) {
                profile.achievements
                    .sortedBy { achievement -> !achievement.isGranted }
            }
            for (achievement in sortedAchievements) {
                AchievementRow(achievement)
            }

            DefaultSpacer(height = 10.dp)
        }
    }
}

@Composable
private fun AvatarAndActionsRow(
    profile: Profile,
    isAddingContact: Boolean,
    onAddContactClick: () -> Unit,
    onChatClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (!profile.isSelf) {
            val (iconRes, iconSize, iconDescriptionRes, onClick) = if (profile.isNotContact) {
                Quadruple(
                    Res.drawable.ic_person_add,
                    26.dp,
                    Res.string.profile_column_description_add_contact,
                    onAddContactClick,
                )
            } else {
                Quadruple(
                    Res.drawable.ic_message_filled,
                    20.dp,
                    Res.string.profile_column_description_chat,
                    onChatClick,
                )
            }

            ActionButton(
                iconRes = iconRes,
                iconSize = iconSize,
                iconDescriptionRes = iconDescriptionRes,
                isLoading = isAddingContact,
                onClick = onClick,
            )
        } else {
            DefaultSpacer(width = 50.dp)
        }

        DefaultSpacer(width = 28.dp)

        DefaultLargeAvatarImage(profile.avatar)

        DefaultSpacer(width = 28.dp)

        ActionButton(
            iconRes = Res.drawable.ic_share,
            iconSize = 26.dp,
            iconDescriptionRes = Res.string.profile_column_description_share,
            isLoading = false,
            onClick = onShareClick,
        )
    }
}

@Composable
fun ActionButton(
    iconRes: DrawableResource,
    iconSize: Dp,
    iconDescriptionRes: StringResource,
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(AppTheme.colors.secondary(0.03f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(20.dp)
                ,
                color = AppTheme.colors.secondary,
                strokeWidth = 3.dp,
            )
        } else {
            DefaultIcon(
                modifier = Modifier
                    .size(iconSize),
                imageRes = iconRes,
                descriptionRes = iconDescriptionRes,
                tint = AppTheme.colors.secondary,
            )
        }
    }
}

@Composable
private fun TotalLikesRow(
    totalLikes: Int,
    color: Color,
    textRes: StringResource,
    icon: DrawableResource,
    topStart: Dp = 0.dp,
    topEnd: Dp = 0.dp,
    bottomEnd: Dp = 0.dp,
    bottomStart: Dp = 0.dp,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(
                color = color(0.1f),
                shape = RoundedCornerShape(topStart, topEnd, bottomStart, bottomEnd),
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .size(64.dp)
                .background(
                    color = color(0.3f),
                    shape = RoundedCornerShape(topStart, topEnd, bottomStart, bottomEnd)
                )
                .padding(16.dp),
            imageVector = vectorResource(icon),
            contentDescription = stringResource(textRes),
            colorFilter = ColorFilter.tint(color),
        )

        DefaultSpacer(width = 12.dp)

        Column {
            Text(
                text = totalLikes.separatedByThreeChars(),
                color = color,
                style = AppTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(textRes),
                color = color,
                style = AppTheme.typography.labelSmall,
            )
        }
    }
}