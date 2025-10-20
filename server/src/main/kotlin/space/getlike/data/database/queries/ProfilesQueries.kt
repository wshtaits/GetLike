package space.getlike.data.database.queries

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.plus
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.upsert
import space.getlike.Emojis
import space.getlike.data.database.tables.ProfilesTable
import space.getlike.models.Avatar
import space.getlike.models.ProfileContent

class ProfilesQueries {

    fun selectProfileById(profileId: String): ProfileContent? = transaction {
        ProfilesTable
            .selectAll()
            .where { ProfilesTable.id eq profileId }
            .mapNotNull { row -> row.fromRow() }
            .singleOrNull()
    }

    fun selectAllByIds(profileIds: Collection<String>): List<ProfileContent> = transaction {
        ProfilesTable
            .selectAll()
            .where { ProfilesTable.id inList profileIds }
            .mapNotNull { row -> row.fromRow() }
    }

    fun selectAvatarUrl(profileId: String): String? = transaction {
        ProfilesTable
            .select(ProfilesTable.avatarUrl)
            .where { ProfilesTable.id eq profileId }
            .limit(1)
            .firstOrNull()
            ?.get(ProfilesTable.avatarUrl)
    }

    fun upsert(profile: ProfileContent): Unit = transaction {
        ProfilesTable
            .upsert { statement ->
                statement[id] = profile.id
                statement[avatarUrl] = profile.avatar.uri
                statement[name] = profile.name
                statement[totalLikesSent] = profile.totalLikesSent
                statement[totalLikesReceived] = profile.totalLikesReceived
            }
    }

    fun updateLikes(profileIdToLikesCount: Map<String, Pair<Int, Int>>): Unit = transaction {
        for ((profileId, likes) in profileIdToLikesCount) {
            val (likesSent, likesReceived) = likes
            ProfilesTable.update(
                where = { ProfilesTable.id eq profileId },
            ) { statement ->
                statement[ProfilesTable.totalLikesSent] = ProfilesTable.totalLikesSent + likesSent
                statement[ProfilesTable.totalLikesReceived] = ProfilesTable.totalLikesReceived + likesReceived
            }
        }
    }

    private fun ResultRow.fromRow(): ProfileContent =
        ProfileContent(
            id = this[ProfilesTable.id],
            avatar = Avatar(
                uri = this[ProfilesTable.avatarUrl],
                fallbackEmoji = Emojis.fromString(this[ProfilesTable.name]),
            ),
            name = this[ProfilesTable.name],
            totalLikesSent = this[ProfilesTable.totalLikesSent],
            totalLikesReceived = this[ProfilesTable.totalLikesReceived],
        )
}