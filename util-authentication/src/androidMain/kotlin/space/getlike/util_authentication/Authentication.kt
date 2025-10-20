package space.getlike.util_authentication

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class Authentication(
    private val activity: Activity?,
    private val auth: FirebaseAuth,
    private val installations: FirebaseInstallations,
) {

    actual val id: String?
        get() = auth.currentUser?.uid

    private val _isAuthedFlow = MutableStateFlow(auth.currentUser != null)
    actual val isAuthedFlow: StateFlow<Boolean> = _isAuthedFlow

    private var verificationId: String? = null
    private var forceResendingToken: ForceResendingToken? = null

    init {
        auth.addAuthStateListener {
            _isAuthedFlow.update { auth.currentUser != null }
        }
    }

    actual suspend fun authenticate(phone: String): Boolean =
        suspendCancellableCoroutine { continuation ->
            if (activity == null) {
                continuation.resumeWithException(IllegalStateException("Activity is null"))
                return@suspendCancellableCoroutine
            }
            val formattedPhone = PhoneFormatter.format(phone)
            val options = buildAuthOptions(
                phoneNumber = formattedPhone,
                activity = activity,
                forceResendingToken = forceResendingToken,
                onVerificationCompleted = { credential ->
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener {
                            signIn(
                                credential = credential,
                                onComplete = { continuation.resume(true) },
                                onFailure = continuation::resumeWithException,
                            )
                        }
                },
                onVerificationFailed = { exception ->
                    continuation.resumeWithException(exception)
                },
                onCodeSent = { verificationId, token ->
                    this@Authentication.verificationId = verificationId
                    this@Authentication.forceResendingToken = token
                    continuation.resume(false)
                }
            )
            PhoneAuthProvider.verifyPhoneNumber(options)
        }

    actual suspend fun verifyCode(code: String) =
        suspendCancellableCoroutine { continuation ->
            val nonNullVerificationId = verificationId
            if (nonNullVerificationId == null) {
                continuation.resumeWithException(IllegalStateException("verificationId is null"))
                return@suspendCancellableCoroutine
            }
            val credential = PhoneAuthProvider.getCredential(nonNullVerificationId, code)
            signIn(
                credential = credential,
                onComplete = { continuation.resume(Unit) },
                onFailure = continuation::resumeWithException,
            )
        }

    actual fun signOut() =
        auth.signOut()

    actual suspend fun getTokenId(): String? =
        suspendCancellableCoroutine { continuation ->
            auth.currentUser
                ?.getIdToken(false)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(task.result?.token)
                    } else {
                        continuation.resume(null)
                    }
                }
                ?: continuation.resume(null)
        }

    actual suspend fun getDeviceId(): String? =
        suspendCancellableCoroutine { continuation ->
            installations.id.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result)
                } else {
                    continuation.resume(null)
                }
            }
        }

    private fun signIn(
        credential: PhoneAuthCredential,
        onComplete: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) =
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete()
                } else {
                    onFailure(task.exception ?: IllegalStateException("Unknown sign in failure"))
                }
            }

    private fun buildAuthOptions(
        phoneNumber: String,
        activity: Activity,
        forceResendingToken: ForceResendingToken?,
        onVerificationCompleted: (PhoneAuthCredential) -> Unit,
        onVerificationFailed: (FirebaseException) -> Unit,
        onCodeSent: (verificationId: String, token: ForceResendingToken) -> Unit,
    ): PhoneAuthOptions =
        PhoneAuthOptions.newBuilder(auth)
            .apply {
                setPhoneNumber(phoneNumber)
                setTimeout(60L, TimeUnit.SECONDS)
                setActivity(activity)
                if (forceResendingToken != null) {
                    setForceResendingToken(forceResendingToken)
                }
                setCallbacks(
                    object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                        override fun onVerificationCompleted(credential: PhoneAuthCredential) =
                            onVerificationCompleted(credential)

                        override fun onVerificationFailed(exception: FirebaseException) =
                            onVerificationFailed(exception)

                        override fun onCodeSent(verificationId: String, token: ForceResendingToken) =
                            onCodeSent(verificationId, token)
                    }
                )
            }
            .build()
}
