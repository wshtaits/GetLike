package space.getlike.util_app_lifecycle

import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationDidEnterBackgroundNotification
import platform.UIKit.UIApplicationWillTerminateNotification

actual class AppLifecycle {

    private var _isForeground = false
    actual val isForeground: Boolean
        get() = _isForeground

    private val center = NSNotificationCenter.defaultCenter

    init {
        center.addObserverForName(
            name = UIApplicationDidBecomeActiveNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue,
            usingBlock = { _isForeground = true },
        )

        center.addObserverForName(
            name = UIApplicationDidEnterBackgroundNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue,
            usingBlock = { _isForeground = false },
        )

        center.addObserverForName(
            name = UIApplicationWillTerminateNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue,
            usingBlock = { _isForeground = false },
        )
    }

    actual fun close() {
        // not supported on iOS
    }
}