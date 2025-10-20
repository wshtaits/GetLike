package space.getlike.util_share.sharing

import space.getlike.util_share.BaseSharing

class InstagramSharing : BaseSharing() {

    override fun isAvailable(): Boolean =
        canOpenUrl("instagram://")

    override fun text(text: String, title: String?, phone: String?) =
        openUrl(url = "https://www.instagram.com/?url=", text = text)
}
