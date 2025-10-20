package space.getlike.util_phone_number

import cocoapods.PhoneNumberKitBridge.PhoneNumberKitBridge

internal actual object PhoneUtil {

    private val phoneNumberKit = PhoneNumberKitBridge()

    actual fun format(number: String): String =
        phoneNumberKit.format(number.preparedForParsing())

    actual fun isValid(number: String): Boolean =
        phoneNumberKit.isValid(number.preparedForParsing())
}
