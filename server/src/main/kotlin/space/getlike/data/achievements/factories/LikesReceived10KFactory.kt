package space.getlike.data.achievements.factories

import space.getlike.data.achievements.factories.base.LikesReceivedFactory
import space.getlike.models.AchievementContent

class LikesReceived10KFactory : LikesReceivedFactory(
    content = AchievementContent.LikesReceived10K,
    likesCount = 10_000,
)