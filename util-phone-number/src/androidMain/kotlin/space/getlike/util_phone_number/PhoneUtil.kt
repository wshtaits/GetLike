package space.getlike.util_phone_number

import com.google.i18n.phonenumbers.PhoneNumberUtil

internal actual object PhoneUtil {

    private val phoneNumberUtil = PhoneNumberUtil.getInstance()

    actual fun format(number: String): String {
        val parsedNumber = phoneNumberUtil.parse(number.preparedForParsing(), null)
        return phoneNumberUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
    }

    actual fun isValid(number: String): Boolean =
        try {
            val parsedNumber = phoneNumberUtil.parse(number.preparedForParsing(), null)
            phoneNumberUtil.isValidNumber(parsedNumber)
        } catch (_: Throwable) {
            false
        }
}