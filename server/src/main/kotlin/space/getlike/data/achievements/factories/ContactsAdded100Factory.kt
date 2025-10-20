package space.getlike.data.achievements.factories

import space.getlike.data.achievements.factories.base.ContactsAddedFactory
import space.getlike.models.AchievementContent

class ContactsAdded100Factory : ContactsAddedFactory(
    content = AchievementContent.ContactsAdded100,
    contactsCount = 100,
)