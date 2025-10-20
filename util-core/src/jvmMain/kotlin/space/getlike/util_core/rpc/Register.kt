package space.getlike.util_core.rpc

import kotlinx.rpc.annotations.Rpc
import kotlinx.rpc.krpc.ktor.server.KrpcRoute

context(route: KrpcRoute)
inline fun <@Rpc reified Service : Any, TDependencies : Any> TDependencies.register(
    noinline factory: (CoreRequest.Bundle) -> Service,
) {
    route.registerService<Service> {
        factory(
            CoreRequest.Bundle(
                dependencies = this,
                applicationCall = route.call,
            ),
        )
    }
}