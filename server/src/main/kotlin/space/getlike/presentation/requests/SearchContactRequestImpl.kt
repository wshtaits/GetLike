package space.getlike.presentation.requests

import space.getlike.presentation.requests.base.Request
import space.getlike.requests.SearchContactRequest

class SearchContactRequestImpl(bundle: Bundle) : Request(bundle), SearchContactRequest {

    override suspend fun execute(phone: String): SearchContactRequest.Result {
        val contactId = deps.profilesRepository.searchContact(phone)
        return when {
            contactId == null -> SearchContactRequest.Result.NotRegistered
            profileId == contactId -> SearchContactRequest.Result.Self
            else -> SearchContactRequest.Result.Success(contactId)
        }
    }
}