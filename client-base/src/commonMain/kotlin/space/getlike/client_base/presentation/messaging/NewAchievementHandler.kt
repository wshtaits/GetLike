package space.getlike.client_base.presentation.messaging

import org.jetbrains.compose.resources.getString
import space.getlike.client_base.dependencies.PresentationDependencies
import space.getlike.deeplinks.ProfileDeeplink
import space.getlike.messaging.NewAchievementsMessaging
import space.getlike.resources.*
import space.getlike.util_messaging.ClientMessagingHandler
import space.getlike.util_notifications.NotificationImage

class NewAchievementHandler(
    private val deps: PresentationDependencies,
) : ClientMessagingHandler<NewAchievementsMessaging.Content>(NewAchievementsMessaging) {

    override suspend fun handle(content: NewAchievementsMessaging.Content) {
        deps.syncRepository.awaitInit()
        content.achievements.forEach { achievement ->
            deps.notificationDisplayer.showNotification(
                id = content.hashCode(),
                title = getString(Res.string.notification_title_new_achievement),
                body = achievement.text,
                smallImage = NotificationImage.Emoji(achievement.emoji),
                largeImage = NotificationImage.Emoji(achievement.emoji),
                colorInt = achievement.color.toInt(),
                deeplink = ProfileDeeplink(deps.profileRepository.self?.id.orEmpty()),
            )
        }
    }
}