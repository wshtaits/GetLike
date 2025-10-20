package space.getlike.util_core.utils

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin

val Job?.isNullOrCompleted: Boolean
    get() = this?.isCompleted ?: true

val Job?.isNotNullAndActive: Boolean
    get() = this?.isActive == true

suspend fun Job.cancelAndIfNotFinished(block: () -> Unit) {
    cancelAndJoin()
    if (isCancelled) {
        block()
    }
}