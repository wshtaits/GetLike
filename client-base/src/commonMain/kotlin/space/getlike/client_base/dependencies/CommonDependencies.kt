package space.getlike.client_base.dependencies

import space.getlike.util_connection.Connection
import space.getlike.util_permissions.PermissionManager
import space.getlike.util_phone_number.PhoneFormatter

interface CommonDependencies {

    val connection: Connection

    val permissionManager: PermissionManager

    val phoneFormatter: PhoneFormatter
}