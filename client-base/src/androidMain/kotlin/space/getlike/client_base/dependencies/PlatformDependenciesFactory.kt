package space.getlike.client_base.dependencies

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.core.net.toUri
import androidx.room.Room
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import space.getlike.client_base.data.database.Database
import space.getlike.client_base.R
import space.getlike.deeplinks.ChatDeeplink.invoke
import space.getlike.deeplinks.SearchContactDeeplink
import space.getlike.util_app_lifecycle.AppLifecycle
import space.getlike.util_app_review.AppReview
import space.getlike.util_app_shortcuts.AppShortcutItem
import space.getlike.util_app_shortcuts.AppShortcuts
import space.getlike.util_authentication.Authentication
import space.getlike.util_connection.Connection
import space.getlike.util_logger.Logger
import space.getlike.util_logger.LoggerImpl
import space.getlike.util_device_contacts.DeviceContactsProvider
import space.getlike.util_image_picker.ImagePicker
import space.getlike.util_messaging.PushTokenProvider
import space.getlike.util_notifications.NotificationDisplayer
import space.getlike.util_permissions.PermissionManager
import space.getlike.util_permissions.PermissionManagerImpl
import space.getlike.util_region.RegionProvider
import space.getlike.util_settings.SettingsImpl
import space.getlike.util_settings.Settings
import space.getlike.util_share.Share
import space.getlike.util_share.ShareImpl
import kotlin.reflect.KClass

actual class PlatformDependenciesFactory(
    private val applicationContext: Context,
    private val activity: ComponentActivity?,
) {

    private val firebaseAnalytics by lazy { FirebaseAnalytics.getInstance(applicationContext) }
    private val firebaseCrashlytics by lazy { FirebaseCrashlytics.getInstance() }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firebaseInstallations by lazy { FirebaseInstallations.getInstance() }
    private val firebaseMessaging by lazy { FirebaseMessaging.getInstance() }

    init {
        FirebaseApp.initializeApp(applicationContext)
    }

    actual fun platform(): String =
        "android"

    actual fun logger(): Logger =
        LoggerImpl(firebaseAnalytics, firebaseCrashlytics)

    actual fun authentication(): Authentication =
        Authentication(activity, firebaseAuth, firebaseInstallations)

    actual fun connection(coroutineScope: CoroutineScope): Connection =
        Connection(applicationContext, coroutineScope)

    actual fun settings(
        name: String,
    ): Settings =
        SettingsImpl(applicationContext, name)

    actual fun share(): Share =
        ShareImpl(checkActivity(ShareImpl::class))

    actual fun database(
        name: String,
    ): Database =
        Room
            .databaseBuilder<Database>(
                context = applicationContext,
                name = applicationContext.getDatabasePath(name).absolutePath,
            )
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .build()

    actual fun deviceContactsProvider(coroutineScope: CoroutineScope): DeviceContactsProvider =
        DeviceContactsProvider(applicationContext.contentResolver)

    actual fun regionProvider(): RegionProvider =
        RegionProvider()

    actual fun pushTokenProvider(): PushTokenProvider =
        PushTokenProvider(firebaseMessaging)

    actual fun notificationDisplayer(): NotificationDisplayer =
        NotificationDisplayer(applicationContext)

    actual fun imagePicker(): ImagePicker =
        ImagePicker(checkActivity(ImagePicker::class))

    actual fun permissionManager(): PermissionManager =
        PermissionManagerImpl(applicationContext, activity)

    actual fun appLifecycle(): AppLifecycle =
        AppLifecycle(activity)

    actual fun appReview(): AppReview =
        AppReview(checkActivity(AppReview::class))

    actual fun appShortcuts(): AppShortcuts =
        AppShortcuts(applicationContext)

    actual fun appShortcutItems(): List<AppShortcutItem> =
        listOf(
            AppShortcutItem(
                titleRes = R.string.app_shortcut_search_contact,
                iconRes = R.drawable.ic_person_search,
                intent = Intent(Intent.ACTION_VIEW).apply {
                    setPackage(applicationContext.packageName)
                    data = SearchContactDeeplink().toUri()
                },
            ),
        )

    private fun checkActivity(requester: KClass<out Any>): ComponentActivity =
        checkNotNull(
            value = activity,
            lazyMessage = { "${requester.simpleName} can be used with Activity only" },
        )
}