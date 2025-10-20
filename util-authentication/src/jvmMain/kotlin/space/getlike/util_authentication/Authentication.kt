package space.getlike.util_authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneIdentifier
import io.ktor.util.Attributes

class Authentication(
    private val firebaseAuth: FirebaseAuth,
) {

    fun verifyTokenId(tokenId: String): Attributes? =
        try {
            val token = firebaseAuth.verifyIdToken(tokenId)
            Attributes().apply {
                put(AuthenticationKey.profileId, token.uid)
                put(AuthenticationKey.avatarUrl, token.picture.orEmpty())
                put(AuthenticationKey.name, token.name.orEmpty())
            }
        } catch (_: Exception) {
            null
        }

    fun getProfileIdByPhone(phone: String): String? =
        try {
            val formattedPhone = PhoneFormatter.format(phone)
            firebaseAuth.getUserByPhoneNumber(formattedPhone).uid
        } catch (_: Exception) {
            null
        }

    fun getProfileIdsByPhones(phones: Collection<String>): Map<String, String> =
        phones
            .map { phone -> PhoneFormatter.format(phone) }
            .map { phone -> PhoneIdentifier(phone) }
            .chunked(100) // firebaseAuth.getUsers handles 100 max at once
            .flatMap { identifiers ->
                firebaseAuth
                    .getUsers(identifiers)
                    .users
                    .mapNotNull { user ->
                        user.uid to user.phoneNumber
                    }
            }
            .toMap()
}