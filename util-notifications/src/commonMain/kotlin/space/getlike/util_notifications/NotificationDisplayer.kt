package space.getlike.util_notifications

expect class NotificationDisplayer {

    suspend fun showNotification(
        id: Int,
        smallImage: NotificationImage.Emoji,
        largeImage: NotificationImage? = null,
        title: String? = null,
        body: String? = null,
        colorInt: Int? = null,
        deeplink: String? = null,
    )

    fun hideNotification(id: Int)
}