package space.getlike.util_share

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.net.toUri
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

abstract class BaseSharing(private val activity: Activity) : Sharing {

    private val packageManager: PackageManager
        get() = activity.packageManager

    protected fun hasPackage(packageName: String): Boolean =
        try {
            packageManager.getPackageInfo(
                @Suppress("INCONSISTENT_PARAMETER_NAME") /* packageName = */ packageName,
                @Suppress("INCONSISTENT_PARAMETER_NAME") /* flags = */ 0,
            )
            true
        } catch (_: Exception) {
            false
        }

    protected fun startIntent(
        action: String = Intent.ACTION_VIEW,
        uriString: String? = null,
        packageName: String? = null,
        type: String? = null,
        mode: Mode = Mode.Direct,
        extras: Map<String, String> = emptyMap(),
    ) =
        try {
            val intent = Intent().let { intent ->
                intent.action = action

                if (uriString != null) {
                    intent.data = uriString.toUri()
                }

                if (packageName != null) {
                    intent.setPackage(packageName)
                }

                if (type != null) {
                    intent.type = type
                }

                for ((name, value) in extras) {
                    intent.putExtra(name, value)
                }

                if (mode is Mode.Chooser) {
                    Intent.createChooser(
                        /* target = */ intent,
                        /* title = */ mode.title,
                    )
                } else {
                    intent
                }
            }

            activity.startActivity(intent)
        } catch (_: Exception) {
            // no op
        }

    protected sealed interface Mode {

        object Direct : Mode

        data class Chooser(val title: String?) : Mode
    }
}