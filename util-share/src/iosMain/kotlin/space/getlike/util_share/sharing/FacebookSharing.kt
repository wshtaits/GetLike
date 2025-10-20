package space.getlike.util_share.sharing

import space.getlike.util_share.BaseSharing

class FacebookSharing : BaseSharing() {

    override fun isAvailable(): Boolean =
        canOpenUrl("fb://")

    override fun text(text: String, title: String?, phone: String?) =
        openUrl(url = "https://www.facebook.com/sharer/sharer.php?u=", text = text)
}
