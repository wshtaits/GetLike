package space.getlike.data.achievements.factories

import space.getlike.data.achievements.factories.base.LikesFromContactsFactory
import space.getlike.models.AchievementContent

class LikesFromContacts100Factory : LikesFromContactsFactory(
    content = AchievementContent.LikesFromContacts100,
    contactsCount = 100,
)