package space.getlike.presentation.requests

import space.getlike.messaging.AddContactMessaging
import space.getlike.models.Profile
import space.getlike.presentation.requests.base.Request
import space.getlike.requests.AddContactRequest

class AddContactRequestImpl(bundle: Bundle) : Request(bundle), AddContactRequest {

    override suspend fun execute(contactId: String): Profile? {
        val oldProfileAchievements = deps.profilesRepository.getProfileAchievements(profileId)
        val oldContactAchievements = deps.profilesRepository.getProfileAchievements(contactId)

        val contact = deps.profilesRepository.addContact(profileId, contactId)
            ?: return null

        deps.messagingSender.send(
            userId = profileId,
            envelope = AddContactMessaging(contact),
            exceptDeviceId = deviceId,
            isWebSocketOnly = true,
        )

        if (!deps.profilesRepository.isContact(profileId = contactId, contactId = profileId)) {
            val profile = deps.profilesRepository.getProfile(profileId) ?: return null
            deps.messagingSender.send(
                userId = contactId,
                envelope = AddContactMessaging(profile),
                exceptDeviceId = deviceId,
                isWebSocketOnly = true,
            )
        }

        deps.achievementsHandler.handle(
            oldAchievements = oldProfileAchievements,
            profileId = profileId,
        )

        deps.achievementsHandler.handle(
            oldAchievements = oldContactAchievements,
            newProfile = contact,
        )

        return contact
    }
}