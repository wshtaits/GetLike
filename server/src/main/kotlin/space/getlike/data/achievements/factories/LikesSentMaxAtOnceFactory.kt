package space.getlike.data.achievements.factories

import space.getlike.data.achievements.factories.base.LikesMaxAtOnceFactory
import space.getlike.models.AchievementContent
import space.getlike.models.Message

class LikesSentMaxAtOnceFactory : LikesMaxAtOnceFactory(
    content = AchievementContent.LikesGivenMaxAtOnce,
    senderIdOrReceiverId = Message::senderId,
)

