package space.getlike.util_core

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
open class Route : NavKey {

    @PublishedApi
    @OptIn(ExperimentalUuidApi::class)
    internal val key = Uuid.random().toString()

    @Serializable
    internal object Empty : Route()
}
