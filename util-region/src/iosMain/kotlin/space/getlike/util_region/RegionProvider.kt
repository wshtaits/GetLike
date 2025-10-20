package space.getlike.util_region

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.countryCode

actual class RegionProvider {

    actual val current: String =
        NSLocale.currentLocale.countryCode ?: "US"
}