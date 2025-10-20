package space.getlike.client_base.data.database.queries

import space.getlike.Emojis
import space.getlike.client_base.data.database.Database
import space.getlike.client_base.data.database.dbos.AchievementDbo
import space.getlike.client_base.data.database.dbos.RegisteredLocalContactIdDbo
import space.getlike.client_base.data.database.dbos.ProfileDbo
import space.getlike.client_base.data.database.queries.base.Queries
import space.getlike.models.Achievement
import space.getlike.models.Avatar
import space.getlike.models.Profile
import space.getlike.models.ProfileStatus

class ProfileQueries(database: Database) : Queries(database) {

    suspend fun selectAll(): List<Profile> = transaction {
        val achievements = achievementDao()
            .selectAll()
            .map { achievementDbo -> achievementDbo.profileId to achievementDbo.fromDbo() }
            .groupBy(
                keySelector = { (profileId, _) -> profileId },
                valueTransform = { (_, achievement) -> achievement },
            )
        profileDao()
            .selectAll()
            .map { profileDbo -> profileDbo.fromDbo(achievements[profileDbo.id].orEmpty()) }
    }

    suspend fun replaceAll(profiles: List<Profile>) = transaction {
        profileDao()
            .replaceAll(profiles.map { profile -> profile.toDbo() })
        achievementDao()
            .replaceAll(
                profiles
                    .flatMap { profile -> profile.achievements }
                    .map { achievement -> achievement.toDbo() },
            )
    }

    suspend fun upsert(profile: Profile) = transaction {
        profileDao().upsert(profile.toDbo())
        achievementDao().upsertAll(
            profile.achievements
                .map { achievement -> achievement.toDbo() },
        )
    }

    suspend fun selectAllRegisteredLocalContactIds(): List<String> = transaction {
        registeredLocalContactIdDao()
            .selectAll()
            .map { dbo -> dbo.fromDbo() }
    }

    suspend fun upsertRegisteredLocalContactIds(localContactIds: List<String>) = transaction {
        registeredLocalContactIdDao()
            .upsertAll(localContactIds.map { localContactId -> localContactId.toDbo() })
    }

    suspend fun deleteAll() = transaction {
        achievementDao().deleteAll()
        profileDao().deleteAll()
        registeredLocalContactIdDao().deleteAll()
    }

    private fun ProfileDbo.fromDbo(
        achievements: List<Achievement>,
    ): Profile =
        Profile(
            id = id,
            avatar = Avatar(
                uri = avatarUrl,
                fallbackEmoji = Emojis.fromString(name),
            ),
            name = name,
            totalLikesSent = totalLikesSent,
            totalLikesReceived = totalLikesReceived,
            status = ProfileStatus.fromString(status),
            achievements = achievements,
        )

    private fun Profile.toDbo(): ProfileDbo =
        ProfileDbo(
            id = id,
            avatarUrl = avatar.uri,
            name = name,
            totalLikesSent = totalLikesSent,
            totalLikesReceived = totalLikesReceived,
            status = status.stringValue,
        )

    private fun AchievementDbo.fromDbo(): Achievement =
        Achievement(
            profileId = profileId,
            isGranted = isGranted,
            progress = progress,
            emoji = emoji,
            color = color,
            text = text,
        )

    private fun Achievement.toDbo(): AchievementDbo =
        AchievementDbo(
            profileId = profileId,
            isGranted = isGranted,
            progress = progress,
            emoji = emoji,
            color = color,
            text = text,
        )

    private fun RegisteredLocalContactIdDbo.fromDbo(): String =
        registeredLocalContactId

    private fun String.toDbo(): RegisteredLocalContactIdDbo =
        RegisteredLocalContactIdDbo(this)
}