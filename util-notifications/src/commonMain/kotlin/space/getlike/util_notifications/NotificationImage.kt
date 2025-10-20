package space.getlike.util_notifications

sealed interface NotificationImage {

    data class Uri(val value: String) : NotificationImage

    data class Emoji(val value: String) : NotificationImage
}