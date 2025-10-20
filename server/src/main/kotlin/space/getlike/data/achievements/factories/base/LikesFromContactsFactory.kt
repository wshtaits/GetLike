package space.getlike.data.achievements.factories.base

import space.getlike.data.achievements.AchievementFactory
import space.getlike.models.AchievementContent

abstract class LikesFromContactsFactory(
    content: AchievementContent,
    contactsCount: Int,
) : AchievementFactory(
    content = content,
    targetValue = contactsCount,
    currentValueProvider = { context ->
        context.messages
            .filter { message -> message.receiverId == context.profileContent.id }
            .map { message -> message.senderId }
            .distinct()
            .size
    },
)