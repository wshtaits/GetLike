package space.getlike.util_permissions

import kotlinx.coroutines.suspendCancellableCoroutine
import space.getlike.util_permissions.handlers.CameraPermissionHandler
import space.getlike.util_permissions.handlers.ContactsPermissionHandler
import kotlin.coroutines.resume

class PermissionManagerImpl : PermissionManager() {

    private val permissionToHandlerMap = mapOf(
        Permission.Camera to CameraPermissionHandler(),
        Permission.Contacts to ContactsPermissionHandler(),
    )

    override fun has(permission: Permission): Boolean =
        permissionToHandlerMap[permission]?.has() ?: false

    override suspend fun request(permission: Permission): Boolean = suspendCancellableCoroutine { continuation ->
        permissionToHandlerMap[permission]?.request(
            onResult = { granted ->
                emitPermission(permission, granted)
                continuation.resume(granted)
            }
        )
    }
}
