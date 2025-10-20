package space.getlike.util_notifications

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationAttachment
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.coroutines.resume

actual class NotificationDisplayer {

    private val center = UNUserNotificationCenter.currentNotificationCenter()

    actual suspend fun showNotification(
        id: Int,
        smallImage: NotificationImage.Emoji,
        largeImage: NotificationImage?,
        title: String?,
        body: String?,
        colorInt: Int?,
        deeplink: String?,
    ) {
        val granted = suspendCancellableCoroutine { cont ->
            center.requestAuthorizationWithOptions(
                options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound,
                completionHandler = { granted, _ -> cont.resume(granted) },
            )
        }

        if (!granted) {
            return
        }

        val content = UNMutableNotificationContent().apply {
            setTitle(title.orEmpty())
            setBody(body.orEmpty())
            setUserInfo(
                if (!deeplink.isNullOrEmpty()) {
                    mapOf("deeplink" to deeplink)
                } else {
                    emptyMap()
                }
            )

            if (largeImage is NotificationImage.Uri) {
                val url = NSURL.URLWithString(largeImage.value) ?: return@apply
                val attachment = UNNotificationAttachment.attachmentWithIdentifier(
                    identifier = NSUUID.UUID().UUIDString,
                    URL = url,
                    options = null,
                    error = null,
                ) ?: return@apply
                setAttachments(listOf(attachment))
            }
        }

        center.addNotificationRequest(
            request = UNNotificationRequest.requestWithIdentifier(
                identifier = id.toString(),
                content = content,
                trigger = null,
            ),
            withCompletionHandler = null,
        )
    }

    actual fun hideNotification(id: Int) {
        center.removeDeliveredNotificationsWithIdentifiers(listOf(id.toString()))
        center.removePendingNotificationRequestsWithIdentifiers(listOf(id.toString()))
    }
}
