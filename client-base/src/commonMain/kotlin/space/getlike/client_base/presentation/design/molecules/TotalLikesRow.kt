package space.getlike.client_base.presentation.design.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.design.atoms.DefaultIcon
import space.getlike.client_base.presentation.design.atoms.DefaultSpacer
import space.getlike.resources.Res
import space.getlike.resources.ic_heart_incoming
import space.getlike.resources.total_likes_row_received
import space.getlike.resources.total_likes_row_sent
import space.getlike.resources.ic_heart_outgoing
import space.getlike.util_core.utils.invoke
import space.getlike.utils.separatedByThreeChars

@Composable
fun TotalLikesRow(
    totalLikesReceived: Int,
    totalLikesSent: Int,
    shouldShowIcons: Boolean,
) {
    Row {
        LikesRow(
            color = AppTheme.colors.accent,
            likesCount = totalLikesReceived,
            iconRes = Res.drawable.ic_heart_incoming,
            descriptionRes = Res.string.total_likes_row_received,
            shouldShowIcon = shouldShowIcons,
        )

        DefaultSpacer(width = 4.dp)

        LikesRow(
            color = AppTheme.colors.primary,
            likesCount = totalLikesSent,
            iconRes = Res.drawable.ic_heart_outgoing,
            descriptionRes = Res.string.total_likes_row_sent,
            shouldShowIcon = shouldShowIcons,
        )
    }
}

@Composable
private fun LikesRow(
    color: Color,
    likesCount: Int,
    iconRes: DrawableResource,
    descriptionRes: StringResource,
    shouldShowIcon: Boolean,
) {
    Row(
        Modifier
            .background(
                color = color(0.1f),
                shape = RoundedCornerShape(8.dp),
            )
            .padding(vertical = 2.dp, horizontal = 6.dp)
        ,
    ) {
        if (shouldShowIcon) {
            DefaultIcon(
                modifier = Modifier
                    .size(16.dp)
                ,
                imageRes = iconRes,
                descriptionRes = descriptionRes,
                tint = color,
            )
            DefaultSpacer(width = 4.dp)
        }
        Text(
            text = likesCount.separatedByThreeChars(),
            color = color,
            style = AppTheme.typography.titleTiny,
        )
    }
}