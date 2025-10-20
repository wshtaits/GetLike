package space.getlike.util_core

import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

object Example {

    private val names = listOf("Floyd", "Robert", "Morris", "Bessie", "Max", "Gloria")

    private val phones = listOf("+79991234567", "+79997654321", "+79990000000", "+79991112233")

    private val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')

    inline fun <reified T> values(vararg values: T): T =
        values.random()

    inline fun <reified T : Enum<T>> enum(): T =
        enumValues<T>().random()

    fun string(length: Int = 10): String =
        CharArray(length, init = { chars.random() }).concatToString()

    fun boolean(): Boolean =
        Random.nextBoolean()

    fun int(range: IntRange? = null): Int =
        if (range != null) {
            Random.nextInt(range)
        } else {
            Random.nextInt()
        }

    fun timestampMinus(daysRange: IntRange): Instant =
        Clock.System.now()
            .minus(Duration.parse("${Random.nextInt(daysRange)}d"))

    fun phone(): String =
        phones.random()

    fun name(): String =
        names.random()
}