package space.getlike.util_messaging

import android.content.Context
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

abstract class MessagingService<TDependencies : Any, TViewModel : MessagingViewModel<TDependencies>>(
    private val dependenciesFactory: (Context, CoroutineScope) -> TDependencies,
    private val viewModelFactory: (TDependencies) -> TViewModel,
) : FirebaseMessagingService() {

    private lateinit var serviceScope: CoroutineScope
    private lateinit var viewModel: TViewModel
    private lateinit var receiver: MessagingReceiver<Unit>

    final override fun onCreate() {
        super.onCreate()

        serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        viewModel = viewModelFactory(dependenciesFactory(applicationContext, serviceScope))
        receiver = MessagingReceiver(viewModel.messageHandlers)

        launch { viewModel.onLaunch() }
    }

    final override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    final override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val name = remoteMessage.data[MessagingEnvelope.KEY_NAME] ?: return
        val contentJson = remoteMessage.data[MessagingEnvelope.KEY_CONTENT_JSON] ?: return
        launch {
            receiver.receive(
                envelope = MessagingEnvelope(
                    name = name,
                    contentJson = contentJson,
                ),
                extra = Unit,
            )
        }
    }

    final override fun onNewToken(token: String) {
        launch { viewModel.onNewToken(token) }
    }

    private fun launch(block: suspend CoroutineScope.() -> Unit): Job =
        serviceScope.launch(block = block)
}