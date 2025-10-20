package space.getlike.util_share.sharing

import space.getlike.util_share.BaseSharing

class TelegramSharing : BaseSharing() {

    override fun isAvailable(): Boolean =
        canOpenUrl("tg://")

    override fun text(text: String, title: String?, phone: String?) =
        openUrl("tg://msg?text=", text)
}
