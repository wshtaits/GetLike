package space.getlike.util_messaging

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

open class Messaging<TContent>(
    internal val name: String,
    private val serializer: KSerializer<TContent>,
) {

    internal fun decodeContent(json: String): TContent =
        Json.decodeFromString(serializer, json)

    internal fun encodeContent(content: TContent): String =
        Json.encodeToString(serializer, content)

    operator fun invoke(
        content: TContent,
    ): MessagingEnvelope =
        MessagingEnvelope(
            name = name,
            contentJson = encodeContent(content),
        )

    companion object Companion {

        operator fun Messaging<Unit>.invoke() =
            invoke(Unit)
    }
}