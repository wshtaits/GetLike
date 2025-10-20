package space.getlike.data.achievements.factories

import space.getlike.data.achievements.factories.base.LikesReceivedFactory
import space.getlike.models.AchievementContent

class LikesReceived1Factory : LikesReceivedFactory(
    content = AchievementContent.LikesReceived1,
    likesCount = 1,
)