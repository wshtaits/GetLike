package space.getlike.client_base.dependencies

import kotlinx.coroutines.CoroutineScope
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
import space.getlike.util_messaging.PushTokenProvider
import space.getlike.util_notifications.NotificationDisplayer
import space.getlike.util_permissions.PermissionManager
import space.getlike.util_region.RegionProvider
import space.getlike.util_settings.Settings
import space.getlike.util_share.Share

expect class PlatformDependenciesFactory {

    fun platform(): String

    fun logger(): Logger

    fun authentication(): Authentication

    fun connection(coroutineScope: CoroutineScope): Connection

    fun settings(name: String): Settings

    fun share(): Share

    fun database(name: String): Database

    fun deviceContactsProvider(coroutineScope: CoroutineScope): DeviceContactsProvider

    fun regionProvider(): RegionProvider

    fun pushTokenProvider(): PushTokenProvider

    fun notificationDisplayer(): NotificationDisplayer

    fun imagePicker(): ImagePicker

    fun permissionManager(): PermissionManager

    fun appLifecycle(): AppLifecycle

    fun appReview(): AppReview

    fun appShortcuts(): AppShortcuts

    fun appShortcutItems(): List<AppShortcutItem>
}