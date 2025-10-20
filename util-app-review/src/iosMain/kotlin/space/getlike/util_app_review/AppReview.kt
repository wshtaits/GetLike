package space.getlike.util_app_review

import platform.StoreKit.SKStoreReviewController
import platform.UIKit.UIApplication
import platform.Foundation.NSURL

actual class AppReview {

    actual fun launch() {
        try {
            SKStoreReviewController.requestReview()
        } catch (_: Exception) {
            val url = NSURL(string = "itms-apps://itunes.apple.com/app/id1234567890?action=write-review")
            UIApplication.sharedApplication.openURL(url)
        }
    }
}
