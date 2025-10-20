package space.getlike.util_authentication

import kotlinx.coroutines.flow.StateFlow

expect class Authentication {

    val id: String?

    val isAuthedFlow: StateFlow<Boolean>

    suspend fun authenticate(phone: String): Boolean

    suspend fun verifyCode(code: String)

    fun signOut()

    internal suspend fun getTokenId(): String?

    internal suspend fun getDeviceId(): String?
}