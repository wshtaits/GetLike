package space.getlike.data.achievements

import space.getlike.models.Achievement
import space.getlike.models.AchievementContent
import space.getlike.utils.separatedByThreeChars

abstract class AchievementFactory(
    private val content: AchievementContent,
    private val targetValue: Int,
    private val currentValueProvider: (AchievementContext) -> Int,
) {

    fun create(context: AchievementContext): Achievement {
        val currentValue = currentValueProvider(context)
        val progressValue = minOf(currentValue, targetValue)

        val progressString = progressValue.separatedByThreeChars()
        val targetString = targetValue.separatedByThreeChars()

        return Achievement(
            profileId = context.profileContent.id,
            isGranted = progressValue == targetValue,
            progress = "$progressString / $targetString",
            emoji = content.emoji,
            color = content.color,
            text = content.text,
        )
    }
}