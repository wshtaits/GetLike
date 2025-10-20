package space.getlike.util_app_shortcuts

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

actual class AppShortcutItem(
    @param:StringRes val titleRes: Int,
    @param:DrawableRes internal val iconRes: Int,
    val intent: Intent,
)