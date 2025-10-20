package space.getlike.client_base.data.repositories

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import space.getlike.client_base.dependencies.DataDependencies
import space.getlike.util_core.Repository

class LoginRepository(
    private val deps: DataDependencies,
) : Repository(deps.coroutineScope) {

    val isAuthedFlow: StateFlow<Boolean>
        get() = deps.authentication.isAuthedFlow

    suspend fun login(phoneNumber: String): Result<Boolean> = executeIoCatching {
        deps.authentication.authenticate(phoneNumber)
    }

    suspend fun verifyCode(code: String) = executeIoCatching {
        deps.authentication.verifyCode(code)
    }

    suspend fun logout() = executeIoCatching {
        deps.authentication.signOut()
        deps.profileQueries.deleteAll()
        deps.messageQueries.deleteAll()

        deps.settings.clear()

        deps.isAutoSyncEnabled = false

        deps.isSyncingFlow.update { false }
        deps.selfProfileFlow.update { null }
        deps.messagesFlow.update { emptyList() }
        deps.contactsFlow.update { emptyList() }
        deps.localContactsFlow.update { emptyList() }
        deps.registeredLocalContactIdsFlow.update { emptyList() }
    }

    suspend fun getTerms(): Result<String> = executeIoCatching {
        deps.getTermsRequest.execute()
    }
}