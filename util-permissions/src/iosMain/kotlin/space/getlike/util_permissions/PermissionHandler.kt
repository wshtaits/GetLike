package space.getlike.util_permissions

interface PermissionHandler {

    fun has(): Boolean

    fun request(onResult: (Boolean) -> Unit)
}