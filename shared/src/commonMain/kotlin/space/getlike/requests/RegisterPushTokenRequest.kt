package space.getlike.requests

import kotlinx.rpc.annotations.Rpc

@Rpc
interface RegisterPushTokenRequest {

    suspend fun execute(pushToken: String)
}