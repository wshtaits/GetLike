package space.getlike.util_messaging

expect class PushTokenProvider {

    suspend fun get(): String?
}