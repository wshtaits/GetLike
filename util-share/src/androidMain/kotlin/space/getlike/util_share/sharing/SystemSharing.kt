package space.getlike.util_share.sharing

import android.app.Activity
import android.content.Intent
import space.getlike.util_share.BaseSharing

class SystemSharing(activity: Activity) : BaseSharing(activity) {

    override fun isAvailable(): Boolean =
        true

    override fun text(
        text: String,
        title: String?,
        phone: String?,
    ) =
        startIntent(
            action = Intent.ACTION_SEND,
            type = "text/plain",
            extras = mapOf(Intent.EXTRA_TEXT to text),
            mode = Mode.Chooser(title),
        )
}