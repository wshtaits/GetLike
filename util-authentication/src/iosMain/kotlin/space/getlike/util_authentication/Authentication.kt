package space.getlike.util_authentication

import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseAuth.FIRPhoneAuthProvider
import cocoapods.FirebaseInstallations.FIRInstallations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class Authentication(
    private val auth: FIRAuth,
    private val installations: FIRInstallations,
) {

    actual val id: String? =
        auth.currentUser()?.uid()

    private val _isAuthedFlow = MutableStateFlow(auth.currentUser() != null)
    actual val isAuthedFlow: StateFlow<Boolean> = _isAuthedFlow

    private var verificationId: String? = null

    init {
        auth.addAuthStateDidChangeListener { _, user ->
            _isAuthedFlow.update { user != null }
        }
    }

    actual suspend fun authenticate(phone: String): Boolean =
        completionCoroutine { completion ->
            val formattedPhone = PhoneFormatter.format(phone)
            FIRPhoneAuthProvider.provider().verifyPhoneNumber(
                phoneNumber = formattedPhone,
                UIDelegate = null,
                completion = completion,
            )
        }.let { false }

    actual suspend fun verifyCode(code: String) {
        val nonNullVerificationId = verificationId ?: throw IllegalStateException("verificationId is null")
        val credential = FIRPhoneAuthProvider.provider()
            .credentialWithVerificationID(nonNullVerificationId, code)
        completionCoroutine { completion ->
            auth.signInWithCredential(credential, completion)
        }
    }

    actual fun signOut() {
        auth.signOut(error = null)
    }

    internal actual suspend fun getTokenId(): String? =
        auth.currentUser()
            ?.let { user -> completionCoroutine(user::getIDTokenWithCompletion) }

    internal actual suspend fun getDeviceId(): String? =
        completionCoroutine(installations::installationIDWithCompletion)

    private suspend fun <T> completionCoroutine(
        block: (completion: (T?, NSError?) -> Unit) -> Unit,
    ): T? =
        suspendCancellableCoroutine { continuation ->
            block { result, error ->
                if (error == null) {
                    continuation.resume(result)
                } else {
                    continuation.resumeWithException(Exception(error.localizedDescription))
                }
            }
        }
}
