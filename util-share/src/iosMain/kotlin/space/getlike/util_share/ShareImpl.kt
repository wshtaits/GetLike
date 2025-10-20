package space.getlike.util_share

import platform.UIKit.UIViewController
import space.getlike.util_share.sharing.*

class ShareImpl(viewController: UIViewController) : Share() {

    override val destinationToSharing = mapOf(
        ShareDestination.System to SystemSharing(viewController),
        ShareDestination.Sms to SmsSharing(),
        ShareDestination.Telegram to TelegramSharing(),
        ShareDestination.WhatsApp to WhatsAppSharing(),
        ShareDestination.Facebook to FacebookSharing(),
        ShareDestination.Instagram to InstagramSharing(),
        ShareDestination.InstagramLite to InstagramSharing(), // same as Instagram
    )
}
