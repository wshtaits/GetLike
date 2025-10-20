package space.getlike.util_messaging

abstract class ClientMessagingHandler<TContent>(
    messaging: Messaging<TContent>,
) : MessagingHandler<TContent, Unit>(messaging) {

    override suspend fun internalHandle(content: TContent, extra: Unit) =
        handle(content)

    protected abstract suspend fun handle(content: TContent)
}