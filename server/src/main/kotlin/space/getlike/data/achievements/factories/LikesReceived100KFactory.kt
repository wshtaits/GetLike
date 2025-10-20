package space.getlike.data.achievements.factories

import space.getlike.data.achievements.factories.base.LikesReceivedFactory
import space.getlike.models.AchievementContent

class LikesReceived100KFactory : LikesReceivedFactory(
    content = AchievementContent.LikesReceived100K,
    likesCount = 100_000,
)