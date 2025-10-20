package space.getlike.util_share.sharing

import space.getlike.util_share.BaseSharing

class SmsSharing : BaseSharing() {

    override fun isAvailable(): Boolean =
        true

    override fun text(text: String, title: String?, phone: String?) {
        openUrl(
            url = if (phone != null) {
                "sms:$phone&body="
            } else {
                "sms:&body="
            },
            text = text,
        )
    }
}
