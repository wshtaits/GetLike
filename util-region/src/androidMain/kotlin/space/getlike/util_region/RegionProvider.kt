package space.getlike.util_region

import java.util.Locale

actual class RegionProvider {

    actual val current: String =
        Locale.getDefault().country
}