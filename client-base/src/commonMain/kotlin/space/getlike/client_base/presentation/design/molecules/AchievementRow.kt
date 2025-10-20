package space.getlike.client_base.presentation.design.molecules

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.design.atoms.DefaultSpacer
import space.getlike.models.Achievement
import space.getlike.models.AchievementContent
import space.getlike.util_core.utils.invoke
import space.getlike.util_core.utils.toPx
import kotlin.math.roundToInt

@Composable
fun AchievementRow(
    content: AchievementContent,
    asHint: Boolean = false,
) {
    AchievementRow(
        emoji = content.emoji,
        color = content.color,
        text = content.text,
        isGranted = true,
        progress = "",
        asHint = asHint
    )
}

@Composable
fun AchievementRow(achievement: Achievement) {
    AchievementRow(
        emoji = achievement.emoji,
        color = achievement.color,
        text = achievement.text,
        isGranted = achievement.isGranted,
        progress = achievement.progress,
        asHint = false,
    )
}

@Composable
private fun AchievementRow(
    emoji: String,
    color: Long,
    text: String,
    isGranted: Boolean,
    progress: String,
    asHint: Boolean,
) {
    val (contentColor, containerColor) = if (isGranted) {
        AppTheme.colors.secondary to Color(color)
    } else {
        AppTheme.colors.secondary(0.7f) to AppTheme.colors.tertiary(0.5f)
    }

    Row(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .background(
                color = containerColor,
                shape = RoundedCornerShape(
                    topStart = if (asHint) {
                        16.dp
                    } else {
                        0.dp
                    },
                    topEnd = 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp,
                ),
            )
            .padding(vertical = 16.dp, horizontal = 16.dp),
    ) {
        Emoji(emoji = emoji, overlayed = !isGranted)

        DefaultSpacer(width = 20.dp)

        Column {
            Text(
                text = text,
                color = contentColor,
                style = AppTheme.typography.labelSmall,
            )

            if (!isGranted) {
                DefaultSpacer(height = 8.dp)
                Text(
                    text = progress,
                    color = AppTheme.colors.secondary(0.5f),
                    style = AppTheme.typography.labelTiny,
                )
            }
        }
    }
}

@Composable
private fun Emoji(
    emoji: String,
    overlayed: Boolean = false,
) {
    val sizeDp = 28.dp
    val sizePx = sizeDp.toPx().roundToInt()
    val overlay = AppTheme.colors.secondary(0.3f)

    val bitmap = remember(emoji, overlayed) {
        emojiToBitmap(emoji, sizePx)
    }

    Canvas(
        modifier = Modifier
            .size(sizeDp)
        ,
    ) {
        drawImage(
            image = bitmap,
            colorFilter = if (overlayed) {
                ColorFilter.tint(color = overlay, blendMode = BlendMode.SrcIn)
            } else {
                null
            },
        )
    }
}

internal expect fun emojiToBitmap(emoji: String, sizePx: Int): ImageBitmap