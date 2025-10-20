package space.getlike.util_logger

actual fun logMessage(message: String) {
    println("Logger: $message")
}

class LoggerImpl : Logger() {

    override fun setCustomKey(key: String, value: String) {
        // no op
    }

    override fun logException(throwable: Throwable) {
        // no op
    }

    override fun logEvent(name: String, params: Map<String, Any>?) {
        // no op
    }
}