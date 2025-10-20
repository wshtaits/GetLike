package space.getlike.util_connection

import kotlinx.coroutines.flow.StateFlow

expect class Connection {

    val isConnectedFlow: StateFlow<Boolean>
}