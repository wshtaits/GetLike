package space.getlike.client_invite

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.design.atoms.DefaultAnimatedContent
import space.getlike.client_base.presentation.design.atoms.DefaultIcon
import space.getlike.client_base.presentation.design.atoms.DefaultMediumAvatarImage
import space.getlike.client_base.presentation.design.atoms.DefaultSpacer
import space.getlike.client_base.presentation.effects.CopyToClipboardEffect
import space.getlike.client_base.presentation.routes.InviteRoute
import space.getlike.resources.*
import space.getlike.util_core.View
import space.getlike.util_core.utils.rippleClickable
import space.getlike.util_core.utils.invoke
import space.getlike.util_share.ShareDestination

class InviteView(bundle: Bundle) : View<InviteViewModel, InviteState>(
    bundle = bundle,
    viewModelFactory = ::InviteViewModel,
) {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Ui() {
        CopyToClipboardEffect.handle()
        Column {
            Header()
            SocialsGrid()
            CopyTextRow()
        }
    }

    @Composable
    private fun Header() {
        val contact = (state.content as? InviteRoute.Content.Contact)?.value ?: return
        val (subtitle, phones) = if (contact.phones.size == 1) {
            contact.phones.first() to emptyList()
        } else {
            null to contact.phones
        }

        DefaultSpacer(height = 8.dp)

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DefaultMediumAvatarImage(contact.avatar)

            DefaultSpacer(width = 8.dp)

            Column {
                Text(
                    text = contact.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = AppTheme.colors.secondary,
                    style = AppTheme.typography.buttonLarge,
                )

                if (subtitle != null) {
                    DefaultSpacer(height = 8.dp)
                    Text(
                        text = subtitle,
                        color = AppTheme.colors.secondary(0.7f),
                        style = AppTheme.typography.labelMedium,
                    )
                }
            }
        }

        DefaultSpacer(height = 4.dp)

        for (phone in phones) {
            PhoneRow(phone)
        }

        DefaultSpacer(height = 8.dp)
    }

    @Composable
    private fun PhoneRow(phone: String) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp)
                .clickable { viewModel.onPhoneClick(phone) }
            ,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = phone == state.selectedPhone,
                onClick = { viewModel.onPhoneClick(phone) },
            )
            Text(
                text = phone,
                color = AppTheme.colors.secondary(0.7f),
                style = AppTheme.typography.labelMedium,
            )
        }
    }

    @Composable
    private fun SocialsGrid() {
        val visuals = remember(state.destinations) {
            state.destinations
                .map { destination -> DestinationVisuals.fromDestination(destination) }
                .sortedBy { destination -> destination.order }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
            ,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(visuals) { visuals ->
                SocialColumn(visuals)
            }
        }

        DefaultSpacer(height = 8.dp)
    }

    @Composable
    private fun SocialColumn(
        visuals: DestinationVisuals,
    ) {
        Column(
            modifier = Modifier
                .rippleClickable(
                    bounded = false,
                    radius = 48.dp,
                    onClick = { viewModel.onSocialClick(visuals.destination) },
                )
                .padding(vertical = 10.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DefaultIcon(
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        color = AppTheme.colors.primary(0.1f),
                        shape = CircleShape,
                    )
                    .padding(12.dp)
                ,
                imageRes = visuals.iconRes,
                descriptionRes = visuals.textRes,
                tint = AppTheme.colors.primary,
            )
            DefaultSpacer(height = 8.dp)
            Text(
                text = stringResource(visuals.textRes),
                color = AppTheme.colors.primary,
                style = AppTheme.typography.labelTiny,
            )
        }
    }

    @Composable
    private fun CopyTextRow() {
        val containerColor by animateColorAsState(
            targetValue = if (state.isTextCopied) {
                AppTheme.colors.accent(0.2f)
            } else {
                AppTheme.colors.secondary(0.1f)
            },
            animationSpec = tween(durationMillis = 200)
        )

        val contentColor by animateColorAsState(
            targetValue = if (state.isTextCopied) {
                AppTheme.colors.accent(1f)
            } else {
                AppTheme.colors.secondary(0.7f)
            },
            animationSpec = tween(durationMillis = 200)
        )

        Row(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(containerColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(color = AppTheme.colors.secondary),
                    onClick = { viewModel.onCopyTextClick() },
                )
                .padding(vertical = 8.dp, horizontal = 16.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                ,
                text = stringResource(Res.string.invite_copy_link_label),
                color = contentColor,
                style = AppTheme.typography.labelMedium,
            )
            DefaultSpacer(width = 12.dp)

            DefaultAnimatedContent(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                ,
                targetState = state.isTextCopied,
                durationMillis = 200,
                scale = true,
            ) { targetState ->
                DefaultIcon(
                    imageRes = if (targetState) {
                        Res.drawable.ic_check
                    } else {
                        Res.drawable.ic_copy
                    },
                    descriptionRes = Res.string.invite_description_copy,
                    tint = contentColor,
                )
            }
        }

        DefaultSpacer(height = 8.dp)
    }

    private enum class DestinationVisuals(
        val order: Int,
        val destination: ShareDestination,
        val iconRes: DrawableResource,
        val textRes: StringResource,
    ) {

        WhatsApp(
            order = 0,
            destination = ShareDestination.WhatsApp,
            iconRes = Res.drawable.ic_social_whatsapp,
            textRes = Res.string.invite_whats_app,
        ),
        Telegram(
            order = 1,
            destination = ShareDestination.Telegram,
            iconRes = Res.drawable.ic_social_telegram,
            textRes = Res.string.invite_telegram,
        ),
        Facebook(
            order = 2,
            destination = ShareDestination.Facebook,
            iconRes = Res.drawable.ic_social_facebook,
            textRes = Res.string.invite_facebook,
        ),
        Instagram(
            order = 3,
            destination = ShareDestination.Instagram,
            iconRes = Res.drawable.ic_social_instagram,
            textRes = Res.string.invite_instagram,
        ),
        InstagramLite(
            order = 4,
            destination = ShareDestination.InstagramLite,
            iconRes = Res.drawable.ic_social_instagram,
            textRes = Res.string.invite_instagram,
        ),
        Sms(
            order = 5,
            destination = ShareDestination.Sms,
            iconRes = Res.drawable.ic_social_sms,
            textRes = Res.string.invite_sms,
        ),
        System(
            order = 6,
            destination = ShareDestination.System,
            iconRes = Res.drawable.ic_more,
            textRes = Res.string.invite_more,
        ),
        ;

        companion object Companion {

            fun fromDestination(destination: ShareDestination): DestinationVisuals =
                entries.find { content -> content.destination == destination } ?: System
        }
    }
}
