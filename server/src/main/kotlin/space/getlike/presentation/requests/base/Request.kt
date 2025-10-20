package space.getlike.presentation.requests.base

import space.getlike.util_core.rpc.CoreRequest
import space.getlike.dependencies.PresentationDependencies
import space.getlike.util_authentication.AuthenticationKey

open class Request(bundle: Bundle) : CoreRequest<PresentationDependencies>(bundle) {

    protected val profileId
        get() = call.attributes[AuthenticationKey.profileId]
    protected val deviceId
        get() = call.attributes[AuthenticationKey.deviceId]
}