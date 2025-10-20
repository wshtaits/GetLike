package space.getlike.models

import kotlinx.serialization.Serializable
import space.getlike.Emojis
import space.getlike.util_core.Example

@Serializable
data class Avatar(
    val uri: String?,
    val fallbackEmoji: String,
) {

    companion object {

        fun example(): Avatar =
            Avatar(
                uri = null,
                fallbackEmoji = Emojis.fromString(Example.name()),
            )
    }
}