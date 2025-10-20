package space.getlike.messaging

import kotlinx.serialization.Serializable
import space.getlike.models.Achievement
import space.getlike.util_messaging.Messaging

object NewAchievementsMessaging : Messaging<NewAchievementsMessaging.Content>(
    name = "NewAchievements",
    serializer = Content.serializer(),
) {

    operator fun invoke(achievements: List<Achievement>) =
        invoke(Content(achievements))

    @Serializable
    data class Content(
        val achievements: List<Achievement>,
    )
}