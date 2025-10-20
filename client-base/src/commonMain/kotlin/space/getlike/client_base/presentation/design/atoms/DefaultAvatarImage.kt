package space.getlike.client_base.presentation.design.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.jetbrains.compose.resources.stringResource
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.models.Avatar
import space.getlike.util_core.utils.applyIfNotNull
import space.getlike.util_core.utils.toSp
import space.getlike.resources.*
import space.getlike.util_core.utils.blinking

@Composable
fun DefaultLargeAvatarImage(
    avatar: Avatar,
) {
    DefaultAvatarImage(
        avatar = avatar,
        size = 112.dp,
        emojiSize = 54.dp,
        radius = 32.dp,
    )
}

@Composable
fun DefaultMediumAvatarImage(
    avatar: Avatar,
) {
    DefaultAvatarImage(
        avatar = avatar,
        size = 60.dp,
        emojiSize = 32.dp,
        radius = 30.dp,
    )
}

@Composable
fun DefaultSmallAvatarImage(
    avatar: Avatar,
    onClick: () -> Unit,
) {
    DefaultAvatarImage(
        avatar = avatar,
        size = 36.dp,
        emojiSize = 16.dp,
        radius = 18.dp,
        onClick = onClick,
    )
}

@Composable
private fun DefaultAvatarImage(
    avatar: Avatar,
    size: Dp,
    emojiSize: Dp,
    radius: Dp,
    onClick: (() -> Unit)? = null,
) {
    SubcomposeAsyncImage(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(radius))
            .background(AppTheme.colors.placeholder)
            .applyIfNotNull(onClick) { onClick ->
                clickable(onClick = onClick)
            }
        ,
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(avatar.uri)
            .crossfade(true)
            .build(),
        contentScale = ContentScale.Crop,
        loading = {
            Emoji(
                blinking = true,
                emoji = avatar.fallbackEmoji,
                size = emojiSize,
            )
        },
        error = {
            Emoji(
                blinking = false,
                emoji = avatar.fallbackEmoji,
                size = emojiSize,
            )
        },
        contentDescription = stringResource(Res.string.default_avatar_image_description),
    )
}

@Composable
private fun Emoji(
    blinking: Boolean,
    emoji: String,
    size: Dp,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
        ,
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .blinking(blinking)
            ,
            text = emoji,
            fontSize = size.toSp(),
        )
    }
}

