package space.getlike.data.achievements.factories

import space.getlike.data.achievements.factories.base.LikesSentFactory
import space.getlike.models.AchievementContent

class LikesSent100KFactory : LikesSentFactory(
    content = AchievementContent.LikesGiven100K,
    likesCount = 100_000,
)