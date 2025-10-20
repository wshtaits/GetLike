package space.getlike.models

enum class MessageStatus(val stringValue: String) {
    Unsent("Unsent"),
    Unread("Unread"),
    Read("Read"),
    ;

    val isUnsent: Boolean
        get() = this == Unsent

    val isUnread: Boolean
        get() = this == Unread

    val isRead: Boolean
        get() = this == Read

    companion object {

        fun fromString(value: String): MessageStatus =
            entries.find { status -> status.stringValue == value } ?: Unsent
    }
}