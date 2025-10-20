package space.getlike.util_messaging

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

actual class PushTokenProvider(
    private val firebaseMessaging: FirebaseMessaging,
) {

    actual suspend fun get(): String? =
        firebaseMessaging.token.await()
}