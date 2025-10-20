package space.getlike.client_base.dependencies

import androidx.room.Room
import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseCrashlytics.FIRCrashlytics
import cocoapods.FirebaseInstallations.FIRInstallations
import cocoapods.FirebaseMessaging.FIRMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIViewController
import space.getlike.client_base.data.database.Database
import space.getlike.util_app_lifecycle.AppLifecycle
import space.getlike.util_app_review.AppReview
import space.getlike.util_app_shortcuts.AppShortcutItem
import space.getlike.util_app_shortcuts.AppShortcuts
import space.getlike.util_logger.Logger
import space.getlike.util_authentication.Authentication
import space.getlike.util_connection.Connection
import space.getlike.util_device_contacts.DeviceContactsProvider
import space.getlike.util_image_picker.ImagePicker
import space.getlike.util_logger.LoggerImpl
import space.getlike.util_messaging.PushTokenProvider
import space.getlike.util_notifications.NotificationDisplayer
import space.getlike.util_permissions.PermissionManager
import space.getlike.util_permissions.PermissionManagerImpl
import space.getlike.util_region.RegionProvider
import space.getlike.util_settings.Settings
import space.getlike.util_settings.SettingsImpl
import space.getlike.util_share.Share
import space.getlike.util_share.ShareImpl

actual class PlatformDependenciesFactory {

    lateinit var viewController: UIViewController

    private val firebaseAuth by lazy { FIRAuth.auth() }
    private val firebaseInstallations by lazy { FIRInstallations.installations() }
    private val firebaseCrashlytics by lazy { FIRCrashlytics.crashlytics() }
    private val firebaseMessaging by lazy { FIRMessaging.messaging() }

    actual fun platform(): String =
        "ios"

    actual fun logger(): Logger =
        LoggerImpl(firebaseCrashlytics)

    actual fun authentication(): Authentication =
        Authentication(firebaseAuth, firebaseInstallations)

    actual fun connection(coroutineScope: CoroutineScope): Connection =
        Connection(coroutineScope)

    actual fun settings(name: String): Settings =
        SettingsImpl(name)

    actual fun share(): Share =
        ShareImpl(viewController)

    actual fun database(name: String): Database {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return Room
            .databaseBuilder<Database>(documentDirectory!!.path + "/${Constants.Database.NAME}")
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    actual fun deviceContactsProvider(coroutineScope: CoroutineScope): DeviceContactsProvider =
        DeviceContactsProvider(coroutineScope)

    actual fun regionProvider(): RegionProvider =
        RegionProvider()

    actual fun pushTokenProvider(): PushTokenProvider =
        PushTokenProvider(firebaseMessaging)

    actual fun notificationDisplayer(): NotificationDisplayer =
        NotificationDisplayer()

    actual fun imagePicker(): ImagePicker =
        ImagePicker(viewController)

    actual fun permissionManager(): PermissionManager =
        PermissionManagerImpl()

    actual fun appLifecycle(): AppLifecycle =
        AppLifecycle()

    actual fun appReview(): AppReview =
        AppReview()

    actual fun appShortcuts(): AppShortcuts =
        AppShortcuts()

    actual fun appShortcutItems(): List<AppShortcutItem> =
        emptyList()
}
