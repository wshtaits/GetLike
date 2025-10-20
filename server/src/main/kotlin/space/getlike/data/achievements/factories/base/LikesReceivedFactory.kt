package space.getlike.data.achievements.factories.base

import space.getlike.data.achievements.AchievementFactory
import space.getlike.models.AchievementContent

abstract class LikesReceivedFactory(
    content: AchievementContent,
    likesCount: Int,
) : AchievementFactory(
    content = content,
    targetValue = likesCount,
    currentValueProvider = { context -> context.profileContent.totalLikesReceived },
)