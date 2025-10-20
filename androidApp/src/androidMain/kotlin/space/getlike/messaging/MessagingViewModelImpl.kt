package space.getlike.messaging

import space.getlike.client_base.dependencies.Dependencies
import space.getlike.util_messaging.MessagingViewModel

class MessagingViewModelImpl(deps: Dependencies) : MessagingViewModel<Dependencies>(deps) {

    override val messageHandlers = deps.messageHandlers

    override suspend fun onLaunch() {
        deps.syncRepository.init()
    }

    override suspend fun onNewToken(token: String) {
        deps.profileRepository.registerPushToken(token)
    }
}