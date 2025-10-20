package space.getlike.data.achievements.factories

import space.getlike.data.achievements.AchievementFactory
import space.getlike.models.AchievementContent

class AvatarSetFactory : AchievementFactory(
    content = AchievementContent.AvatarSet,
    targetValue = 1,
    currentValueProvider = { context -> if (context.profileContent.avatar.uri != null) 1 else 0 },
)