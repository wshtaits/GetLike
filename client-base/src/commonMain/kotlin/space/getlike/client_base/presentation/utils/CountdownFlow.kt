package space.getlike.client_base.presentation.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import space.getlike.util_core.utils.clockNow
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

fun countdownFlow(
    durationMillis: Long,
    periodMillis: Long,
): Flow<Long> = flow {
    var now = Instant.clockNow()
    val end = now.plus(durationMillis.milliseconds)

    while (now < end) {
        val timeLeftMillis = (end - now).inWholeMilliseconds
        emit(timeLeftMillis)

        if (timeLeftMillis >= periodMillis) {
            delay(periodMillis)
        } else {
            delay(timeLeftMillis)
        }

        now = Instant.clockNow()
    }

    emit(0)
}
