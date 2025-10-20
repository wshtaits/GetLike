package space.getlike.client_base.presentation.messaging

import space.getlike.client_base.dependencies.PresentationDependencies
import space.getlike.messaging.ProfileChangedMessaging
import space.getlike.models.Profile
import space.getlike.util_messaging.ClientMessagingHandler

class ProfileChangedHandler(
    private val deps: PresentationDependencies,
) : ClientMessagingHandler<Profile>(ProfileChangedMessaging) {

    override suspend fun handle(content: Profile) {
        deps.profileRepository.updateLocal(content)
    }
}