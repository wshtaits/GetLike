package space.getlike.data.achievements

import space.getlike.models.Message
import space.getlike.models.ProfileContent

data class AchievementContext(
    val profileContent: ProfileContent,
    val contactIds: List<String>,
    val beingContactIds: List<String>,
    val messages: List<Message>,
)