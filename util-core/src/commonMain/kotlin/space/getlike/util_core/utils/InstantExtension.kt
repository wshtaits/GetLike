package space.getlike.util_core.utils

import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

fun Instant.Companion.clockNowPlus(
    seconds: Int = 0,
) =
    Clock.System.now()
        .plus(Duration.parse("${seconds}s"))

fun Instant.Companion.clockNow(): Instant =
    Clock.System.now()