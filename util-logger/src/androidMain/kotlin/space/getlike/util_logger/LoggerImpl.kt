package space.getlike.util_logger

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

actual fun logMessage(message: String) {
    Log.d("Logger", message)
}

class LoggerImpl(
    private val analytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics,
) : Logger() {

    override fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }

    override fun logException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    override fun logEvent(name: String, params: Map<String, Any>?) {
        val bundle = params?.let {
            Bundle().apply {
                for ((key, value) in params) {
                    when (value) {
                        is String -> putString(key, value)
                        is Int -> putInt(key, value)
                        is Long -> putLong(key, value)
                        is Double -> putDouble(key, value)
                        is Float -> putFloat(key, value)
                        is Boolean -> putBoolean(key, value)
                    }
                }
            }
        }
        analytics.logEvent(name, bundle)
    }
}