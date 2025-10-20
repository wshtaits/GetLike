package space.getlike.client_base.dependencies

import space.getlike.client_base.data.repositories.ChatRepository
import space.getlike.client_base.data.repositories.LoginRepository
import space.getlike.client_base.data.repositories.ProfileRepository
import space.getlike.client_base.data.repositories.SyncRepository
import space.getlike.util_core.Broadcast
import space.getlike.util_app_lifecycle.AppLifecycle
import space.getlike.util_app_review.AppReview
import space.getlike.util_app_shortcuts.AppShortcutItem
import space.getlike.util_app_shortcuts.AppShortcuts
import space.getlike.util_image_picker.ImagePicker
import space.getlike.util_logger.Logger
import space.getlike.util_notifications.NotificationDisplayer
import space.getlike.util_region.RegionProvider
import space.getlike.util_share.Share

interface PresentationDependencies : CommonDependencies {

    val logger: Logger

    val share: Share

    val regionProvider: RegionProvider

    val notificationDisplayer: NotificationDisplayer

    val imagePicker: ImagePicker

    val appLifecycle: AppLifecycle

    val appReview: AppReview

    val appShortcuts: AppShortcuts

    val appShortcutItems: List<AppShortcutItem>

    val broadcast: Broadcast

    val chatRepository: ChatRepository
    val syncRepository: SyncRepository
    val loginRepository: LoginRepository
    val profileRepository: ProfileRepository
}