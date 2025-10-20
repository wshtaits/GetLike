package space.getlike.data.achievements.factories

import space.getlike.data.achievements.factories.base.LikesReceivedFactory
import space.getlike.models.AchievementContent

class LikesReceived1000KFactory : LikesReceivedFactory(
    content = AchievementContent.LikesReceived1000K,
    likesCount = 1_000_000,
)