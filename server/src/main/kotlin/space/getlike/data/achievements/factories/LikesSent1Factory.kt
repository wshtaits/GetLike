package space.getlike.data.achievements.factories

import space.getlike.data.achievements.factories.base.LikesSentFactory
import space.getlike.models.AchievementContent

class LikesSent1Factory : LikesSentFactory(
    content = AchievementContent.LikesGiven1,
    likesCount = 1,
)