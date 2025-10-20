package space.getlike.util_app_shortcuts

import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationShortcutIcon
import platform.UIKit.UIApplicationShortcutItem
import platform.UIKit.shortcutItems

actual class AppShortcuts {

    actual suspend fun setup(items: List<AppShortcutItem>) {
        val shortcutItems = items
            .take(4)
            .map { item ->
                UIApplicationShortcutItem(
                    type = item.title,
                    localizedTitle = item.title,
                    localizedSubtitle = null,
                    icon = UIApplicationShortcutIcon.iconWithType(item.iconType),
                    userInfo = null,
                )
            }
        UIApplication.sharedApplication.shortcutItems = shortcutItems
    }
}