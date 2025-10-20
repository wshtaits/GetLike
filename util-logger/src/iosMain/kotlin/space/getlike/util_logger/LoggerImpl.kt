package space.getlike.util_logger

import cocoapods.FirebaseAnalytics.FIRAnalytics
import cocoapods.FirebaseCrashlytics.FIRCrashlytics
import platform.Foundation.NSError
import platform.Foundation.NSLocalizedDescriptionKey
import platform.Foundation.NSLog

actual fun logMessage(message: String) =
    NSLog(message)

class LoggerImpl(
    private val crashlytics: FIRCrashlytics,
) : Logger() {

    override fun setCustomKey(key: String, value: String) =
        crashlytics.setCustomValue(key, value)

    override fun logException(throwable: Throwable) =
        crashlytics.recordError(
            NSError(
                domain = "KotlinThrowable",
                code = -1,
                userInfo = mapOf(
                    NSLocalizedDescriptionKey to throwable.message,
                    "stackTrace" to throwable.stackTraceToString(),
                )
            )
        )

    override fun logEvent(name: String, params: Map<String, Any>?) =
        @Suppress("UNCHECKED_CAST")
        FIRAnalytics.logEventWithName(name, params as Map<Any?, *>)
}