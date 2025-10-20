package space.getlike.util_messaging

import androidx.lifecycle.ViewModel

abstract class MessagingViewModel<TDependencies>(
    protected val deps: TDependencies,
) : ViewModel() {

    abstract val messageHandlers: List<ClientMessagingHandler<*>>

    abstract suspend fun onLaunch()

    abstract suspend fun onNewToken(token: String)
}