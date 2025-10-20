package space.getlike.util_core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterIsInstance

class Broadcast {

    @PublishedApi
    internal val flow = MutableSharedFlow<Any>()

    suspend fun send(broadcast: Any) {
        flow.emit(broadcast)
    }

    inline fun <reified T : Any> receive(): Flow<T> =
        flow.filterIsInstance<T>()
}