package space.getlike.models

import kotlinx.serialization.Serializable
import space.getlike.util_core.Example

@Serializable
data class LocalContact(
    val id: String,
    val avatar: Avatar,
    val name: String,
    val phones: List<String>,
) {
    companion object Companion {

        fun example(): LocalContact = LocalContact(
            id = Example.string(),
            avatar = Avatar.example(),
            name = Example.name(),
            phones = List(2) { Example.phone() },
        )
    }
}