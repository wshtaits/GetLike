package space.getlike.util_share.sharing

import android.app.Activity
import android.content.Intent
import android.net.Uri
import space.getlike.util_share.BaseSharing

class WhatsAppSharing(activity: Activity) : BaseSharing(activity) {

    override fun isAvailable(): Boolean =
        hasPackage(PACKAGE_NAME)

    override fun text(
        text: String,
        title: String?,
        phone: String?,
    ) =
        if (phone != null) {
            startIntent(
                uriString = "https://wa.me/$phone?text=${Uri.encode(text)}",
                packageName = PACKAGE_NAME,
            )
        } else {
            startIntent(
                action = Intent.ACTION_SEND,
                packageName = PACKAGE_NAME,
                type = "text/plain",
                extras = mapOf(Intent.EXTRA_TEXT to text),
            )
        }

    private companion object {
        const val PACKAGE_NAME = "com.whatsapp"
    }
}