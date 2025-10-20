package space.getlike.util_share

import platform.Foundation.NSCharacterSet
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.URLQueryAllowedCharacterSet
import platform.Foundation.create
import platform.Foundation.stringByAddingPercentEncodingWithAllowedCharacters
import platform.UIKit.UIApplication

abstract class BaseSharing : Sharing {

    protected fun canOpenUrl(url: String): Boolean {
        val nsUrl = NSURL.URLWithString(url) ?: return false
        return UIApplication.sharedApplication.canOpenURL(nsUrl)
    }

    protected fun openUrl(url: String, text: String) {
        val formattedText = NSString
            .create(text)
            .stringByAddingPercentEncodingWithAllowedCharacters(NSCharacterSet.URLQueryAllowedCharacterSet)
        val url = NSURL.URLWithString("$url$formattedText") ?: return
        UIApplication.sharedApplication.openURL(url)
    }
}