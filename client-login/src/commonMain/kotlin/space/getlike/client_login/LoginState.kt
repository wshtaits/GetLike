package space.getlike.client_login

import kotlinx.serialization.Serializable

@Serializable
data class LoginState(
    val step: Step,
    val phone: String,
    val code: String,
    val isPhoneValid: Boolean,
    val isCodeValid: Boolean,
    val hasConnection: Boolean,
    val authState: AuthState,
    val secondsLeft: Long,
) {

    enum class Step(val number: Int) {
        Phone(0),
        Code(1),
    }

    enum class AuthState {
        Idle,
        Login,
        Verification,
    }
}