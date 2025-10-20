package space.getlike.util_phone_number

internal expect object PhoneUtil {

    fun format(number: String): String

    fun isValid(number: String): Boolean
}

internal fun String.preparedForParsing(): String =
    if (isNotBlank() && this[0] != '+') {
        "+$this"
    } else {
        this
    }
