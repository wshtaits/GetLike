package space.getlike.presentation.requests

import space.getlike.messaging.ReadMessagesMessaging
import space.getlike.messaging.SendMessagesMessaging
import space.getlike.models.LocalContact
import space.getlike.models.Message
import space.getlike.presentation.requests.base.Request
import space.getlike.requests.SyncRequest
import space.getlike.util_authentication.AuthenticationKey
import space.getlike.util_core.utils.keysAndValues

class SyncRequestImpl(bundle: Bundle) : Request(bundle), SyncRequest {

    override suspend fun execute(
        pushToken: String?,
        markedReadContactIds: List<String>,
        unsentMessages: List<Message>,
        localContacts: List<LocalContact>,
    ): SyncRequest.Result {
        val messageReceiverIds = unsentMessages
            .map { message -> message.receiverId }
        val (registeredContactIds, registeredLocalContactIds) =
            deps.profilesRepository
                .getContactIdToLocalContactIdMap(profileId, localContacts)
                .keysAndValues

        val contactIds = (messageReceiverIds + registeredContactIds).distinct()

        val oldProfileAchievements = deps.profilesRepository.getProfileAchievements(profileId)
        val oldContactsAchievements = deps.profilesRepository.getAchievementsByProfileIds(contactIds)

        sync(pushToken, registeredContactIds, markedReadContactIds, unsentMessages)

        deps.achievementsHandler.handle(
            oldAchievements = oldProfileAchievements,
            profileId = profileId,
            exceptDeviceId = deviceId,
            forceSendProfileChanged = true,
        )
        deps.achievementsHandler.handle(
            oldAchievementsMap = oldContactsAchievements,
            profileIds = contactIds,
            forceSendProfileChanged = true,
        )

        return getExisted(registeredLocalContactIds) ?: createNew()
    }

    private suspend fun sync(
        pushToken: String?,
        contactIds: Set<String>,
        markedReadContactIds: List<String>,
        unsentMessages: List<Message>,
    ) {
        if (pushToken != null) {
            deps.messagingRepository.registerPushToken(profileId, deviceId, pushToken)
        }

        deps.profilesRepository.addContacts(profileId, contactIds)

        deps.chatRepository.markRead(senderIds = markedReadContactIds, receiverId = profileId)

        val messages = deps.chatRepository.addMessages(unsentMessages)

        deps.messagingSender.send(
            userId = profileId,
            envelope = ReadMessagesMessaging(
                senderIds = markedReadContactIds,
                receiverId = profileId,
            ),
            exceptDeviceId = deviceId,
            isWebSocketOnly = true,
        )

        for (contactId in markedReadContactIds) {
            deps.messagingSender.send(
                userId = contactId,
                envelope = ReadMessagesMessaging(
                    senderId = contactId,
                    receiverId = profileId,
                ),
                isWebSocketOnly = true,
            )
        }

        deps.messagingSender.send(
            userId = profileId,
            envelope = SendMessagesMessaging(messages),
            exceptDeviceId = deviceId,
            isWebSocketOnly = true,
        )

        for ((receiverId, messages) in messages.groupBy { message -> message.receiverId }) {
            deps.messagingSender.send(
                userId = receiverId,
                envelope = SendMessagesMessaging(messages),
            )
        }
    }

    private fun getExisted(registeredLocalContactIds: List<String>): SyncRequest.Result? {
        val profile = deps.profilesRepository.getProfile(profileId = profileId, selfProfileId = profileId)
            ?: return null
        val contactIds = deps.profilesRepository.getContactIdsAndBeingContactIds(profileId)
        return SyncRequest.Result(
            profile = profile,
            contacts = deps.profilesRepository.getProfiles(profileIds = contactIds, selfProfileId = profileId),
            messages = deps.chatRepository.getMessagesByProfileId(profileId),
            registeredLocalContactIds = registeredLocalContactIds,
        )
    }

    private fun createNew(): SyncRequest.Result {
        val profile = deps.profilesRepository.createProfile(
            profileId = profileId,
            avatarUri = call.attributes[AuthenticationKey.avatarUrl],
            name = call.attributes[AuthenticationKey.name],
        )
        return SyncRequest.Result(
            profile = profile,
            contacts = emptyList(),
            messages = emptyList(),
            registeredLocalContactIds = emptyList(),
        )
    }
}