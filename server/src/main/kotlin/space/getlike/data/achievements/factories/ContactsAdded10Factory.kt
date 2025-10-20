package space.getlike.data.achievements.factories

import space.getlike.data.achievements.factories.base.ContactsAddedFactory
import space.getlike.models.AchievementContent

class ContactsAdded10Factory : ContactsAddedFactory(
    content = AchievementContent.ContactsAdded10,
    contactsCount = 10,
)