package space.getlike.util_core.rpc

import io.ktor.server.application.ApplicationCall

abstract class CoreRequest<TDependencies : Any>(
    bundle: Bundle,
) {

    @Suppress("UNCHECKED_CAST")
    protected val deps: TDependencies =
        bundle.dependencies as TDependencies

    protected val call: ApplicationCall =
        bundle.applicationCall

    @ConsistentCopyVisibility
    data class Bundle @PublishedApi internal constructor(
        internal val dependencies: Any,
        internal val applicationCall: ApplicationCall,
    )
}