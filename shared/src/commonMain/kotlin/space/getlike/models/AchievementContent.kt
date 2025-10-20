package space.getlike.models

import kotlinx.serialization.Serializable

@Serializable
data class AchievementContent(
    val emoji: String,
    val color: Long,
    val text: String,
) {

    fun toAchievement(
        profileId: String,
        isGranted: Boolean,
        progress: String,
    ): Achievement =
        Achievement(
            profileId = profileId,
            isGranted = isGranted,
            progress = progress,
            emoji = emoji,
            color = color,
            text = text,
        )

    companion object {

        val Goal: AchievementContent =
            AchievementContent(
                emoji = "💪",
                color = 0xFFCFECFF,
                text = "Reach 1\u00A0000\u00A0000 likes in total — show your dedication!",
            )

        val GoalFriend: AchievementContent =
            AchievementContent(
                emoji = "🤝",
                color = 0xFFCFECFF,
                text = "Help your friends collect 1\u00A0000\u00A0000 likes together",
            )

        val AvatarSet: AchievementContent =
            AchievementContent(
                emoji = "🖼️",
                color = 0xFFF9E3E6,
                text = "Uploaded avatar — now you’ve got a face!",
            )

        val ContactsAdded1: AchievementContent =
            AchievementContent(
                emoji = "🎉",
                color = 0xFFE2DAF9,
                text = "You made your very first friend — the journey begins!",
            )

        val ContactsAdded10: AchievementContent =
            AchievementContent(
                emoji = "🤝",
                color = 0xFFD2E4FB,
                text = "10 friends already! Looks like you're building a squad.",
            )

        val ContactsAdded100: AchievementContent =
            AchievementContent(
                emoji = "🌐",
                color = 0xFFF7E7B2,
                text = "100 friends — you're a true connector of people!",
            )

        val ContactsMutual1: AchievementContent =
            AchievementContent(
                emoji = "💞",
                color = 0xFFF5CFE2,
                text = "Got your first mutual friend — connection goes both ways!",
            )

        val ContactsMutual10: AchievementContent =
            AchievementContent(
                emoji = "🔗",
                color = 0xFFD2E4FB,
                text = "10 mutual friends — your bonds are getting stronger!",
            )

        val ContactsMutual100: AchievementContent =
            AchievementContent(
                emoji = "🕸️",
                color = 0xFFF6DFAF,
                text = "100 mutual friends! You're the heart of a whole network!",
            )

        val LikesFromContacts1 = AchievementContent(
            emoji = "💌",
            color = 0xFFF9E3E6,
            text = "Received like from a friend — someone noticed you!",
        )

        val LikesFromContacts10 = AchievementContent(
            emoji = "🎊",
            color = 0xFFFDF2C5,
            text = "Received likes from 10 different friends — you’re well-loved.",
        )

        val LikesFromContacts100 = AchievementContent(
            emoji = "🏆",
            color = 0xFFFBE29D,
            text = "Received likes from 100 different friends — your circle truly appreciates you!",
        )

        val LikesGiven1: AchievementContent =
            AchievementContent(
                emoji = "👍",
                color = 0xFFCFEDE2,
                text = "Your first like — sharing is caring!",
            )

        val LikesGiven10K: AchievementContent =
            AchievementContent(
                emoji = "💥",
                color = 0xFFD4E5FB,
                text = "10\u00A0000 likes given — that's some serious tapping!",
            )

        val LikesGiven100K: AchievementContent =
            AchievementContent(
                emoji = "✨",
                color = 0xFFF7E7B2,
                text = "100\u00A0000 likes — spreading good vibes everywhere.",
            )

        val LikesGiven1000K: AchievementContent =
            AchievementContent(
                emoji = "⚡",
                color = 0xFFF7C6B5,
                text = "1\u00A0000\u00A0000 likes — you're unstoppable!",
            )

        val LikesGivenMaxAtOnce: AchievementContent =
            AchievementContent(
                emoji = "🚀",
                color = 0xFFD8D9F9,
                text = "Sent 10\u00A0000 likes at once — full power mode!",
            )

        val LikesReceived1: AchievementContent =
            AchievementContent(
                emoji = "🎁",
                color = 0xFFF6D3C1,
                text = "Your very first like — someone cares about you!",
            )

        val LikesReceived10K: AchievementContent =
            AchievementContent(
                emoji = "🔥",
                color = 0xFFF8C9A4,
                text = "10\u00A0000 likes received — you're on fire!",
            )

        val LikesReceived100K: AchievementContent =
            AchievementContent(
                emoji = "🏅",
                color = 0xFFE6D4F6,
                text = "100\u00A0000 likes — pure popularity unlocked.",
            )

        val LikesReceived1000K: AchievementContent =
            AchievementContent(
                emoji = "👑",
                color = 0xFFD8E3FA,
                text = "1\u00A0000\u00A0000 likes — you’re a true legend!",
            )

        val LikesReceivedMaxAtOnce: AchievementContent =
            AchievementContent(
                emoji = "🌟",
                color = 0xFFD2F1FB,
                text = "Received 10\u00A0000 likes at once — friend went all in!",
            )
    }
}