package space.getlike.util_share.sharing

import platform.UIKit.UIActivityViewController
import platform.UIKit.UIViewController
import space.getlike.util_share.BaseSharing

class SystemSharing(
    private val viewController: UIViewController,
) : BaseSharing() {

    override fun isAvailable(): Boolean =
        true

    override fun text(text: String, title: String?, phone: String?) {
        val activityViewController = UIActivityViewController(
            activityItems = listOf(text),
            applicationActivities = null,
        )
        viewController.presentViewController(
            activityViewController,
            animated = true,
            completion = null,
        )
    }
}
