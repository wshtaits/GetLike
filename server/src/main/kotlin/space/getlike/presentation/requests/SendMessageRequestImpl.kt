package space.getlike.presentation.requests

import space.getlike.messaging.SendMessagesMessaging
import space.getlike.models.Message
import space.getlike.presentation.requests.base.Request
import space.getlike.requests.SendMessageRequest

class SendMessageRequestImpl(bundle: Bundle) : Request(bundle), SendMessageRequest {

    override suspend fun execute(message: Message): Message? {
        val oldProfileAchievements = deps.profilesRepository.getProfileAchievements(profileId)
        val oldContactAchievements = deps.profilesRepository.getProfileAchievements(message.receiverId)

        val addedMessage = deps.chatRepository.addMessage(message) ?: return null

        deps.messagingSender.send(
            userId = profileId,
            envelope = SendMessagesMessaging(addedMessage),
            exceptDeviceId = deviceId,
            isWebSocketOnly = true,
        )
        deps.messagingSender.send(
            userId = addedMessage.receiverId,
            envelope = SendMessagesMessaging(addedMessage),
        )

        deps.achievementsHandler.handle(
            oldAchievements = oldProfileAchievements,
            profileId = profileId,
            forceSendProfileChanged = true,
        )
        deps.achievementsHandler.handle(
            oldAchievements = oldContactAchievements,
            profileId = addedMessage.receiverId,
            forceSendProfileChanged = true,
        )

        return addedMessage
    }
}