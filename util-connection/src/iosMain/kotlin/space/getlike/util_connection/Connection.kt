package space.getlike.util_connection

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_get_main_queue

actual class Connection(
    private val coroutineScope: CoroutineScope,
) {

    private val _isConnectedFlow = MutableStateFlow(false)
    actual val isConnectedFlow: StateFlow<Boolean> get() = _isConnectedFlow

    init {
        val monitor = nw_path_monitor_create()
        nw_path_monitor_set_queue(monitor, dispatch_get_main_queue())
        nw_path_monitor_set_update_handler(monitor) { path ->
            coroutineScope.launch {
                val isConnected = nw_path_get_status(path) == nw_path_status_satisfied
                _isConnectedFlow.emit(isConnected)
            }
        }
        nw_path_monitor_start(monitor)
        coroutineScope.coroutineContext.job.invokeOnCompletion {
            nw_path_monitor_cancel(monitor)
        }
    }
}