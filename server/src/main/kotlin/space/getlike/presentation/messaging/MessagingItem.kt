package space.getlike.presentation.messaging

import kotlin.time.Instant

data class MessagingItem(
    val profileId: String,
    val deviceId: String,
    val pushToken: String,
    val lastSeen: Instant,
)