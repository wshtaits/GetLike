package space.getlike.data.achievements.factories

import space.getlike.data.achievements.factories.base.ContactsMutualFactory
import space.getlike.models.AchievementContent

class ContactsMutual100Factory : ContactsMutualFactory(
    content = AchievementContent.ContactsMutual100,
    contactsCount = 100,
)