package space.getlike.util_messaging

import kotlinx.serialization.Serializable

@Serializable
@ConsistentCopyVisibility
data class MessagingEnvelope internal constructor(
    val name: String,
    val contentJson: String,
) {

    companion object Companion {
        const val KEY_NAME = "name"
        const val KEY_CONTENT_JSON = "contentJson"
    }
}