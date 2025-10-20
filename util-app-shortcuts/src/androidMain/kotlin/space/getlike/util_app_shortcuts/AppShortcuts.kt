package space.getlike.util_app_shortcuts

import android.content.Context
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import kotlin.jvm.java

actual class AppShortcuts(
    private val context: Context,
) {

    actual suspend fun setup(items: List<AppShortcutItem>) {
        context.getSystemService(ShortcutManager::class.java).dynamicShortcuts = items
            .map { shortcut ->
                ShortcutInfo
                    .Builder(
                        /* context = */ context,
                        /* id = */ shortcut.hashCode().toString(),
                    )
                    .apply {
                        setShortLabel(context.getString(shortcut.titleRes))
                        setIcon(Icon.createWithResource(context, shortcut.iconRes))
                        setIntent(shortcut.intent)
                    }
                    .build()
            }
    }
}
