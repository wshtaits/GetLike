package space.getlike.util_permissions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

abstract class PermissionManager {

    private val permissionFlow = MutableSharedFlow<Pair<Permission, Boolean>>(extraBufferCapacity = 1)

    abstract fun has(permission: Permission): Boolean

    abstract suspend fun request(permission: Permission): Boolean

    fun observe(permission: Permission): Flow<Boolean> =
        permissionFlow
            .filter { (changedPermission, _) -> changedPermission == permission }
            .map { (_, granted) -> granted }
            .distinctUntilChanged()

    protected fun emitPermission(permission: Permission, granted: Boolean) =
        permissionFlow.tryEmit(permission to granted)
}