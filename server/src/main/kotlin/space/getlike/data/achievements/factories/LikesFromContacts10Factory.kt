package space.getlike.data.achievements.factories

import space.getlike.data.achievements.factories.base.LikesFromContactsFactory
import space.getlike.models.AchievementContent

class LikesFromContacts10Factory : LikesFromContactsFactory(
    content = AchievementContent.LikesFromContacts10,
    contactsCount = 10,
)