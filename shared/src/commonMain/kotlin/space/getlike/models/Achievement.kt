package space.getlike.models

import kotlinx.serialization.Serializable
import space.getlike.util_core.Example

@Serializable
data class Achievement(
    val profileId: String,
    val isGranted: Boolean,
    val progress: String,
    val emoji: String,
    val color: Long,
    val text: String,
) {

    companion object {

        fun example(): Achievement =
            Example.values(
                AchievementContent.Goal,
                AchievementContent.AvatarSet,
                AchievementContent.ContactsAdded1,
                AchievementContent.LikesGiven1,
                AchievementContent.LikesReceived1,
            ).toAchievement(
                profileId = Example.string(),
                isGranted = Example.boolean(),
                progress = Example.values("10/100", "0/1", "5323/10000"),
            )
    }
}