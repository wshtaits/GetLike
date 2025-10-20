package space.getlike.models

import kotlinx.serialization.Serializable
import space.getlike.util_core.Example

@Serializable
data class Profile(
    val id: String,
    val avatar: Avatar,
    val name: String,
    val totalLikesSent: Int,
    val totalLikesReceived: Int,
    val status: ProfileStatus,
    val achievements: List<Achievement>,
) {

    val isSelf: Boolean =
        status.isSelf

    val isContact: Boolean =
        status.isContact

    val isNotContact: Boolean =
        status.isNotContact

    companion object {

        fun example(): Profile = Profile(
            id = Example.string(),
            avatar = Avatar.example(),
            name = Example.name(),
            totalLikesSent = Example.int(range = 10..1000000),
            totalLikesReceived = Example.int(range = 10..1000000),
            status = Example.enum(),
            achievements = List(5) { Achievement.example() },
        )
    }
}