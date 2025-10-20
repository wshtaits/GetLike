package space.getlike.util_share.sharing

import android.app.Activity
import android.net.Uri
import space.getlike.util_share.BaseSharing

class FacebookSharing(activity: Activity) : BaseSharing(activity) {

    override fun isAvailable(): Boolean =
        hasPackage(PACKAGE_NAME)

    override fun text(
        text: String,
        title: String?,
        phone: String?,
    ) =
        startIntent(
            uriString = "https://www.facebook.com/sharer/sharer.php?u=${Uri.encode(text)}",
            packageName = PACKAGE_NAME,
        )

    private companion object {
        const val PACKAGE_NAME = "com.facebook.katana"
    }
}