package space.getlike.client_base.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import space.getlike.client_base.dependencies.DataDependencies
import space.getlike.models.LocalContact
import space.getlike.models.Profile
import space.getlike.requests.SearchContactRequest
import space.getlike.util_core.Repository
import space.getlike.util_core.utils.addOrReplaceFirstBy
import space.getlike.util_core.utils.replaceFirstBy

class ProfileRepository(
    private val deps: DataDependencies,
) : Repository(deps.coroutineScope) {

    val self: Profile?
        get() = deps.selfProfileFlow.value

    val nonNullSelfProfileFlow: Flow<Profile> =
        deps.selfProfileFlow
            .filterNotNull()

    val unregisteredLocalContactsFlow: StateFlow<List<LocalContact>> =
        combine(
            deps.localContactsFlow,
            deps.registeredLocalContactIdsFlow,
            transform = { localContacts, registeredLocalContactIds ->
                localContacts.filter { localContact -> localContact.id !in registeredLocalContactIds }
            },
        ).stateIn(emptyList())

    suspend fun updateSelf(
        newName: String? = null,
        newAvatarBytes: ByteArray? = null,
    ) = executeIoCatching {
        val profile = deps.editProfileRequest.execute(newName, newAvatarBytes)
            ?: return@executeIoCatching
        updateLocal(profile)
    }

    suspend fun updateLocal(profile: Profile) = executeIo {
        deps.profileQueries.upsert(profile)
        if (profile.id == deps.selfProfileFlow.value?.id) {
            deps.selfProfileFlow.update { profile }
        } else {
            deps.contactsFlow.replaceFirstBy(
                predicate = { contact -> contact.id == profile.id },
                value = profile,
            )
        }
    }

    suspend fun searchContact(phone: String): Result<SearchContactRequest.Result?> = executeIoCatching {
        deps.searchContactRequest.execute(phone)
    }

    suspend fun addContact(contactId: String) = executeIoCatching {
        val contact = deps.addContactRequest.execute(contactId)
        if (contact != null) {
            addContactLocal(contact)
        }
    }

    suspend fun addContactLocal(contact: Profile) = executeIo {
        deps.contactsFlow.addOrReplaceFirstBy(
            predicate = { profile -> profile.id == contact.id },
            value = contact,
        )
        deps.profileQueries.upsert(contact)
    }

    suspend fun registerPushToken(pushToken: String) = executeIoCatching {
        deps.registerPushTokenRequest.execute(pushToken)
    }
}