package space.getlike.data.achievements.factories

import space.getlike.data.achievements.factories.base.LikesSentFactory
import space.getlike.models.AchievementContent

class LikesSent10KFactory : LikesSentFactory(
    content = AchievementContent.LikesGiven10K,
    likesCount = 10_000,
)