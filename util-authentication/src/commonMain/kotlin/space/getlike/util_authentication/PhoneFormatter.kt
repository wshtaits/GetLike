package space.getlike.util_authentication

internal object PhoneFormatter {

    fun format(phone: String) = if (phone.isNotBlank() && phone[0] != '+') {
        "+$phone"
    } else {
        phone
    }
}