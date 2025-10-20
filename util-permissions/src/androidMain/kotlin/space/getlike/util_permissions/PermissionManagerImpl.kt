package space.getlike.util_permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PermissionManagerImpl(
    private val context: Context,
    private val activity: ComponentActivity?,
) : PermissionManager() {

    override fun has(permission: Permission): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            permission.toAndroidPermission(),
        ) == PackageManager.PERMISSION_GRANTED

    override suspend fun request(permission: Permission): Boolean =
        suspendCancellableCoroutine { continuation ->
            if (has(permission)) {
                continuation.resume(true)
            } else {
                if (activity == null) {
                    continuation.resume(false)
                    return@suspendCancellableCoroutine
                }
                activity.activityResultRegistry
                    .register(
                        key = permission.name,
                        contract = ActivityResultContracts.RequestPermission(),
                        callback = { granted ->
                            continuation.resume(granted)
                            emitPermission(permission, granted)
                        },
                    )
                    .launch(permission.toAndroidPermission())
            }
        }

    private fun Permission.toAndroidPermission(): String =
        when (this) {
            Permission.Camera -> Manifest.permission.CAMERA
            Permission.Contacts -> Manifest.permission.READ_CONTACTS
        }
}