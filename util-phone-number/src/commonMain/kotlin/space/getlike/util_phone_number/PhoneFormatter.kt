package space.getlike.util_phone_number

class PhoneFormatter {

    fun format(number: String): String =
        PhoneUtil.format(number)

    fun isValid(number: String): Boolean =
        PhoneUtil.isValid(number)
}