package space.getlike.client_base.presentation.messaging

import space.getlike.client_base.dependencies.PresentationDependencies
import space.getlike.messaging.AddContactMessaging
import space.getlike.models.Profile
import space.getlike.util_messaging.ClientMessagingHandler

class AddContactHandler(
    private val deps: PresentationDependencies,
) : ClientMessagingHandler<Profile>(AddContactMessaging) {

    override suspend fun handle(content: Profile) =
        deps.profileRepository.addContactLocal(content)
}