package space.getlike.util_share

import android.app.Activity
import space.getlike.util_share.sharing.*

class ShareImpl(activity: Activity) : Share() {

    override val destinationToSharing = mapOf(
        ShareDestination.System to SystemSharing(activity),
        ShareDestination.Sms to SmsSharing(activity),
        ShareDestination.Telegram to TelegramSharing(activity),
        ShareDestination.WhatsApp to WhatsAppSharing(activity),
        ShareDestination.Facebook to FacebookSharing(activity),
        ShareDestination.Instagram to InstagramSharing(activity),
        ShareDestination.InstagramLite to InstagramLiteSharing(activity),
    )
}
