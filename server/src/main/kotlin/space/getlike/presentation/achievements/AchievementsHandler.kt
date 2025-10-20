package space.getlike.presentation.achievements

import space.getlike.dependencies.PresentationDependencies
import space.getlike.messaging.NewAchievementsMessaging
import space.getlike.messaging.ProfileChangedMessaging
import space.getlike.models.Achievement
import space.getlike.models.Profile
import space.getlike.models.ProfileStatus

class AchievementsHandler(
    private val deps: PresentationDependencies,
) {

    suspend fun handle(
        oldAchievementsMap: Map<String, List<Achievement>>,
        profileIds: Collection<String>,
        forceSendProfileChanged: Boolean = false,
    ) {
        val newProfiles = deps.profilesRepository.getProfiles(profileIds)
            .associateBy { profile -> profile.id }
        for (profileId in profileIds) {
            val oldAchievements = oldAchievementsMap[profileId] ?: continue
            val newProfile = newProfiles[profileId] ?: continue
            handle(
                oldAchievements = oldAchievements,
                newProfile = newProfile,
                forceSendProfileChanged = forceSendProfileChanged,
            )
        }
    }

    suspend fun handle(
        oldAchievements: List<Achievement>,
        profileId: String,
        exceptDeviceId: String? = null,
        forceSendProfileChanged: Boolean = false,
    ) = handle(
        oldAchievements = oldAchievements,
        newProfile = null,
        profileId = profileId,
        exceptDeviceId = exceptDeviceId,
        forceSendProfileChanged = forceSendProfileChanged,
    )

    suspend fun handle(
        oldAchievements: List<Achievement>,
        newProfile: Profile,
        exceptDeviceId: String? = null,
        forceSendProfileChanged: Boolean = false,
    ) = handle(
        oldAchievements = oldAchievements,
        newProfile = newProfile,
        profileId = newProfile.id,
        exceptDeviceId = exceptDeviceId,
        forceSendProfileChanged = forceSendProfileChanged,
    )

    private suspend fun handle(
        oldAchievements: List<Achievement>,
        newProfile: Profile?,
        profileId: String,
        exceptDeviceId: String?,
        forceSendProfileChanged: Boolean,
    ) {
        val nonNullNewProfile = newProfile
            ?: deps.profilesRepository.getProfile(profileId)
            ?: return

        val (isAchievementsChanged, grantedAchievements) =
            compareAchievements(oldAchievements, nonNullNewProfile.achievements)

        if (isAchievementsChanged || forceSendProfileChanged) {
            deps.messagingSender.send(
                userId = profileId,
                envelope = ProfileChangedMessaging(nonNullNewProfile.copy(status = ProfileStatus.Self)),
                exceptDeviceId = exceptDeviceId,
                isWebSocketOnly = true,
            )

            val beingContactIds = deps.profilesRepository.getBeingContactIds(profileId)
            val contactIds = deps.profilesRepository.getContactIds(profileId) - beingContactIds

            deps.messagingSender.send(
                userIds = beingContactIds,
                envelope = ProfileChangedMessaging(nonNullNewProfile.copy(status = ProfileStatus.Contact)),
                isWebSocketOnly = true,
            )
            deps.messagingSender.send(
                userIds = contactIds,
                envelope = ProfileChangedMessaging(nonNullNewProfile.copy(status = ProfileStatus.NotContact)),
                isWebSocketOnly = true,
            )
        }

        if (grantedAchievements.isNotEmpty()) {
            deps.messagingSender.send(
                userId = profileId,
                envelope = NewAchievementsMessaging(grantedAchievements),
            )
        }
    }

    private fun compareAchievements(
        oldAchievements: List<Achievement>,
        newAchievements: List<Achievement>,
    ): Pair<Boolean, List<Achievement>> {
        val changedAchievements = newAchievements - oldAchievements
        return changedAchievements.isNotEmpty() to changedAchievements.filter { achievement -> achievement.isGranted }
    }
}