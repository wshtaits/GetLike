package space.getlike.util_app_lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle

actual class AppLifecycle(
    private val activity: Activity?
) {

    private var _isForeground: Boolean = false
    actual val isForeground: Boolean
        get() = _isForeground

    init {
        activity?.application?.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {

                override fun onActivityStarted(startedActivity: Activity) {
                    if (activity == startedActivity) {
                        _isForeground = true
                    }
                }

                override fun onActivityStopped(stoppedActivity: Activity) {
                    if (activity == stoppedActivity) {
                        _isForeground = false
                    }
                }

                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    // no op
                }

                override fun onActivityDestroyed(activity: Activity) {
                    // no op
                }

                override fun onActivityPaused(activity: Activity) {
                    // no op
                }

                override fun onActivityResumed(activity: Activity) {
                    // no op
                }

                override fun onActivitySaveInstanceState(activity: Activity, savedInstanceState: Bundle) {
                    // no op
                }
            }
        )
    }

    actual fun close() {
        activity?.finish()
    }
}