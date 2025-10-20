package space.getlike.data.achievements.factories.base

import space.getlike.data.achievements.AchievementFactory
import space.getlike.models.AchievementContent
import space.getlike.models.Chat
import space.getlike.models.Message

abstract class LikesMaxAtOnceFactory(
    content: AchievementContent,
    senderIdOrReceiverId: (Message) -> String,
) : AchievementFactory(
    content = content,
    targetValue = Chat.MAX_LIKES_COUNT,
    currentValueProvider = { context ->
        context.messages
            .filter { message -> senderIdOrReceiverId(message) == context.profileContent.id }
            .maxOfOrNull { message -> message.likesCount }
            ?: 0
    },
)