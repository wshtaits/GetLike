package space.getlike.presentation.requests

import space.getlike.models.Profile
import space.getlike.presentation.requests.base.Request
import space.getlike.requests.EditProfileRequest

class EditProfileRequestImpl(bundle: Bundle) : Request(bundle), EditProfileRequest {

    override suspend fun execute(
        newName: String?,
        newAvatarBytes: ByteArray?,
    ): Profile? {
        val oldAchievements = deps.profilesRepository.getProfileAchievements(profileId)

        val newProfile = deps.profilesRepository.editProfile(profileId, newName, newAvatarBytes)
            ?: return null

        deps.achievementsHandler.handle(
            oldAchievements = oldAchievements,
            newProfile = newProfile,
            exceptDeviceId = deviceId,
            forceSendProfileChanged = true,
        )

        return newProfile
    }
}