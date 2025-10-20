package space.getlike.presentation.requests

import space.getlike.presentation.requests.base.Request
import space.getlike.requests.RegisterPushTokenRequest

class RegisterPushTokenRequestImpl(bundle: Bundle) : Request(bundle), RegisterPushTokenRequest {

    override suspend fun execute(pushToken: String) =
        deps.messagingRepository.registerPushToken(profileId, deviceId, pushToken)
}