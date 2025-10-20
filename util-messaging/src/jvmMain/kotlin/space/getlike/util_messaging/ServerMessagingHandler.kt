package space.getlike.util_messaging

import io.ktor.server.application.ApplicationCall

abstract class ServerMessagingHandler<TContent>(
    messaging: Messaging<TContent>,
) : MessagingHandler<TContent, ApplicationCall>(messaging) {

    override suspend fun internalHandle(content: TContent, extra: ApplicationCall) =
        handle(content, extra)

    protected abstract suspend fun handle(content: TContent, call: ApplicationCall)
}