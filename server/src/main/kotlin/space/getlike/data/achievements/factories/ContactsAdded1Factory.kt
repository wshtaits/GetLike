package space.getlike.data.achievements.factories

import space.getlike.data.achievements.factories.base.ContactsAddedFactory
import space.getlike.models.AchievementContent

class ContactsAdded1Factory : ContactsAddedFactory(
    content = AchievementContent.ContactsAdded1,
    contactsCount = 1,
)