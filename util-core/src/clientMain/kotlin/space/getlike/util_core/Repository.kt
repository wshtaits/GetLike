package space.getlike.util_core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class Repository(
    private val coroutineScope: CoroutineScope,
) {

    protected suspend fun <T> executeIoCatching(
        onFinally: suspend CoroutineScope.() -> Unit = {},
        onTry: suspend CoroutineScope.() -> T,
    ): Result<T> =
        withContext(Dispatchers.IO) {
            try {
                Result.success(onTry())
            } catch (t: Throwable) {
                Result.failure(t)
            } finally {
                onFinally()
            }
        }

    protected suspend fun <T> executeIo(block: suspend CoroutineScope.() -> T): T =
        withContext(Dispatchers.IO, block)

    protected fun <T> Flow<T>.launchCollect(collector: suspend (T) -> Unit): Job =
        launch { collect(collector) }

    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job =
        coroutineScope.launch(block = block)

    fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> =
        stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = initialValue,
        )
}