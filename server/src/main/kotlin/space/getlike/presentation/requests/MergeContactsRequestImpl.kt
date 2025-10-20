package space.getlike.presentation.requests

import space.getlike.models.LocalContact
import space.getlike.presentation.requests.base.Request
import space.getlike.requests.MergeContactsRequest
import space.getlike.util_core.utils.keysAndValues

class MergeContactsRequestImpl(bundle: Bundle) : Request(bundle), MergeContactsRequest {

    override suspend fun execute(localContacts: List<LocalContact>): MergeContactsRequest.Result {
        val (registeredContactIds, registeredLocalContactIds) =
            deps.profilesRepository
                .getContactIdToLocalContactIdMap(profileId, localContacts)
                .keysAndValues

        val oldProfileAchievements = deps.profilesRepository.getProfileAchievements(profileId)
        val oldContactsAchievements = deps.profilesRepository.getAchievementsByProfileIds(registeredContactIds)

        deps.profilesRepository.addContacts(profileId, registeredContactIds)

        deps.achievementsHandler.handle(
            oldAchievements = oldProfileAchievements,
            profileId = profileId,
            exceptDeviceId = deviceId,
            forceSendProfileChanged = true,
        )
        deps.achievementsHandler.handle(
            oldAchievementsMap = oldContactsAchievements,
            profileIds = registeredContactIds,
            forceSendProfileChanged = true,
        )

        val profile = deps.profilesRepository.getProfile(profileId = profileId, selfProfileId = profileId)
            ?: error("Profile is null")
        val contactIds = deps.profilesRepository.getContactIdsAndBeingContactIds(profileId)

        return MergeContactsRequest.Result(
            profile = profile,
            contacts = deps.profilesRepository.getProfiles(profileIds = contactIds, selfProfileId = profileId),
            registeredLocalContactIds = registeredLocalContactIds,
        )
    }
}