package space.getlike.util_app_lifecycle

expect class AppLifecycle {

    val isForeground: Boolean

    fun close()
}