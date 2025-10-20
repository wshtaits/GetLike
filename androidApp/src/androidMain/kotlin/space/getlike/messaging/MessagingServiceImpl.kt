package space.getlike.messaging

import android.annotation.SuppressLint
import androidx.lifecycle.SavedStateHandle
import space.getlike.client_base.dependencies.Dependencies
import space.getlike.client_base.dependencies.PlatformDependenciesFactory
import space.getlike.util_messaging.MessagingService

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessagingServiceImpl : MessagingService<Dependencies, MessagingViewModelImpl>(
    dependenciesFactory = { applicationContext, scope ->
        Dependencies(
            savedStateHandle = SavedStateHandle(),
            factory = PlatformDependenciesFactory(
                applicationContext = applicationContext,
                activity = null,
            ),
            coroutineScope = scope,
        )
    },
    viewModelFactory = ::MessagingViewModelImpl,
)