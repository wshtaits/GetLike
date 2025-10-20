package space.getlike.requests

import kotlinx.rpc.annotations.Rpc

@Rpc
interface GetTermsRequest {

    suspend fun execute(): String
}