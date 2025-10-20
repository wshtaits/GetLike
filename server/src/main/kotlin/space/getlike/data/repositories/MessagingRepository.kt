package space.getlike.data.repositories

import space.getlike.dependencies.DataDependencies
import space.getlike.presentation.messaging.MessagingItem
import space.getlike.util_core.utils.clockNow
import kotlin.time.Instant

class MessagingRepository(
    private val deps: DataDependencies,
) {

    fun refreshLastSeen(profileId: String, deviceId: String) =
        deps.messagingQueries.upsertLastSeen(
            profileId = profileId,
            deviceId = deviceId,
            lastSeen = Instant.clockNow(),
        )

    fun registerPushToken(profileId: String, deviceId: String, pushToken: String) =
        deps.messagingQueries.upsert(
            MessagingItem(
                profileId = profileId,
                deviceId = deviceId,
                pushToken = pushToken,
                lastSeen = Instant.clockNow(),
            )
        )

    fun getItemsByProfileIds(profileIds: List<String>): List<MessagingItem> =
        deps.messagingQueries.selectAllByProfileIds(profileIds)
}