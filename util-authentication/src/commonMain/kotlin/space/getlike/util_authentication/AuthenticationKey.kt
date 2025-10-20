package space.getlike.util_authentication

import io.ktor.util.AttributeKey

object AuthenticationKey {

    val profileId = AttributeKey<String>("profileId")
    val deviceId = AttributeKey<String>("deviceId")
    val avatarUrl = AttributeKey<String>("picture")
    val name = AttributeKey<String>("name")
}
