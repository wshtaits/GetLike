package space.getlike.util_share.sharing

import space.getlike.util_share.BaseSharing

class WhatsAppSharing : BaseSharing() {

    override fun isAvailable(): Boolean =
        canOpenUrl("whatsapp://")

    override fun text(text: String, title: String?, phone: String?) {
        openUrl(
            url = if (phone != null) {
                "https://wa.me/$phone?text="
            } else {
                "whatsapp://send?text="
            },
            text = text,
        )
    }
}
