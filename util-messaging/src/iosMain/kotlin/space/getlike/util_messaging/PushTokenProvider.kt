package space.getlike.util_messaging

import cocoapods.FirebaseMessaging.FIRMessaging
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class PushTokenProvider(
    private val messaging: FIRMessaging,
) {

    actual suspend fun get(): String? =
        suspendCancellableCoroutine { cont ->
            messaging.tokenWithCompletion { token, _ -> cont.resume(token) }
        }
}
