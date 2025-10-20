package space.getlike.data.achievements.factories

import space.getlike.data.achievements.factories.base.LikesSentFactory
import space.getlike.models.AchievementContent

class LikesSent1000KFactory : LikesSentFactory(
    content = AchievementContent.LikesGiven1000K,
    likesCount = 1_000_000,
)