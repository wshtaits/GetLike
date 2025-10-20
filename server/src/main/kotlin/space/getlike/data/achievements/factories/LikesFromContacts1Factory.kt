package space.getlike.data.achievements.factories

import space.getlike.data.achievements.factories.base.LikesFromContactsFactory
import space.getlike.models.AchievementContent

class LikesFromContacts1Factory : LikesFromContactsFactory(
    content = AchievementContent.LikesFromContacts1,
    contactsCount = 1,
)