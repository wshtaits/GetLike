package space.getlike.presentation.requests

import space.getlike.data.Resources
import space.getlike.presentation.requests.base.Request
import space.getlike.requests.GetTermsRequest

class GetTermsRequestImpl(bundle: Bundle) : Request(bundle), GetTermsRequest {

    override suspend fun execute(): String =
        Resources.Terms.text
}