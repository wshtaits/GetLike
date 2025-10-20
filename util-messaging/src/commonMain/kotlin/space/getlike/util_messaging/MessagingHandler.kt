package space.getlike.util_messaging

abstract class MessagingHandler<TContent, TExtra> internal constructor(
    internal val messaging: Messaging<TContent>,
) {

    internal suspend fun internalHandle(envelope: MessagingEnvelope, extra: TExtra) =
        internalHandle(
            content = messaging.decodeContent(envelope.contentJson),
            extra = extra,
        )

    internal abstract suspend fun internalHandle(content: TContent, extra: TExtra)
}