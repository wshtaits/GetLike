package space.getlike.util_connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

actual class Connection(
    context: Context,
    private val scope: CoroutineScope,
) {

    private val _isConnectedFlow by lazy { MutableStateFlow(hasInternet()) }
    actual val isConnectedFlow: StateFlow<Boolean> by lazy { _isConnectedFlow }

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    init {
        connectivityManager.registerNetworkCallback(
            /* request = */ NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(),
            /* networkCallback = */ object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    // no op: network is available, but internet access is not guaranteed
                }

                override fun onLost(network: Network) {
                    emitIsConnected(false)
                }

                override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                    val hasInternet = hasInternet(capabilities)
                    emitIsConnected(hasInternet)
                }
            }
        )
    }

    private fun hasInternet(): Boolean {
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return hasInternet(capabilities)
    }

    private fun hasInternet(capabilities: NetworkCapabilities): Boolean =
        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

    private fun emitIsConnected(value: Boolean) =
        scope.launch { _isConnectedFlow.emit(value) }
}
