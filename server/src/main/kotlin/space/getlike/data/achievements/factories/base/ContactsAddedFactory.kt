package space.getlike.data.achievements.factories.base

import space.getlike.data.achievements.AchievementFactory
import space.getlike.models.AchievementContent

abstract class ContactsAddedFactory(
    content: AchievementContent,
    contactsCount: Int,
) : AchievementFactory(
    content = content,
    targetValue = contactsCount,
    currentValueProvider = { context -> context.contactIds.size },
)