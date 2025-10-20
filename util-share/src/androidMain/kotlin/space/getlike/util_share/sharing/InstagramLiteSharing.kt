package space.getlike.util_share.sharing

import android.app.Activity
import android.content.Intent
import space.getlike.util_share.BaseSharing

class InstagramLiteSharing(activity: Activity) : BaseSharing(activity) {

    override fun isAvailable(): Boolean =
        hasPackage(PACKAGE_NAME)

    override fun text(text: String, title: String?, phone: String?) =
        startIntent(
            action = Intent.ACTION_SEND,
            packageName = PACKAGE_NAME,
            type = "text/plain",
            extras = mapOf(Intent.EXTRA_TEXT to text)
        )

    private companion object {
        const val PACKAGE_NAME = "com.instagram.lite"
    }
}