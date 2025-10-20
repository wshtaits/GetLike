package space.getlike.util_share.sharing

import android.app.Activity
import space.getlike.util_share.BaseSharing

class SmsSharing(activity: Activity) : BaseSharing(activity) {

    override fun isAvailable(): Boolean =
        true

    override fun text(
        text: String,
        title: String?,
        phone: String?,
    ) =
        startIntent(
            uriString = if (phone != null) {
                "sms:$phone"
            } else {
                "sms:"
            },
            extras = mapOf("sms_body" to text),
        )
}