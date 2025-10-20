package space.getlike.models

import kotlinx.serialization.Serializable

@Serializable
data class ProfileContent(
    val id: String,
    val avatar: Avatar,
    val name: String,
    val totalLikesSent: Int,
    val totalLikesReceived: Int,
) {

    fun toProfile(
        status: ProfileStatus,
        achievements: List<Achievement>,
    ): Profile =
        Profile(
            id = id,
            avatar = avatar,
            name = name,
            totalLikesSent = totalLikesSent,
            totalLikesReceived = totalLikesReceived,
            status = status,
            achievements = achievements,
        )
}