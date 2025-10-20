package space.getlike.presentation.requests.base

import kotlinx.rpc.annotations.Rpc
import kotlinx.rpc.krpc.ktor.server.KrpcRoute
import space.getlike.dependencies.PresentationDependencies
import space.getlike.util_core.rpc.CoreRequest
import space.getlike.util_core.rpc.register

context(route: KrpcRoute)
inline fun <@Rpc reified Service : Any> PresentationDependencies.register(
    noinline factory: (CoreRequest.Bundle) -> Service,
) = register<Service, PresentationDependencies>(factory)