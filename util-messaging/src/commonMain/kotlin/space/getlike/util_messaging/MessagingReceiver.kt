package space.getlike.util_messaging

class MessagingReceiver<TExtra>(
    handlers: List<MessagingHandler<*, TExtra>>,
) {

    private val messageNameToHandlers = handlers
        .associateBy { handler -> handler.messaging.name }

    internal suspend fun receive(envelope: MessagingEnvelope, extra: TExtra) {
        @Suppress("UNCHECKED_CAST")
        val handler = messageNameToHandlers[envelope.name] as? MessagingHandler<Any?, TExtra> ?: return
        handler.internalHandle(envelope, extra)
    }
}